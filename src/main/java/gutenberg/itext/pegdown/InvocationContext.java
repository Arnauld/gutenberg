package gutenberg.itext.pegdown;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.itext.AlternateTableRowBackground;
import gutenberg.itext.CellStyler;
import gutenberg.itext.FontAwesomeAdapter;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.itext.Sections;
import gutenberg.pegdown.plugin.Attributes;
import gutenberg.pegdown.plugin.AttributesNode;
import gutenberg.pygments.Pygments;
import gutenberg.pygments.StyleSheet;
import gutenberg.pygments.Token;
import gutenberg.pygments.styles.FriendlyStyle;
import gutenberg.util.RGB;
import gutenberg.util.VariableResolver;
import org.pegdown.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static gutenberg.itext.FontCopier.copyFont;
import static gutenberg.itext.ITextUtils.inconsolata;
import static gutenberg.itext.ITextUtils.toColor;
import static java.util.Arrays.fill;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InvocationContext {

    private Logger log = LoggerFactory.getLogger(InvocationContext.class);

    private final Map<Class<?>, Processor> processors;
    private final Processor processorDefault;
    private final Stack<Font> fontStack;
    private final Stack<TableInfos> tableStack;
    private final FontAwesomeAdapter fontAwesome;
    private final StyleSheet styleSheet;
    private final PygmentsAdapter pygments;
    private final Sections sections;
    private final BaseFont verbatimFont;
    private final Stack<CellStyler> cellStylerStack;
    private final Font defaultFont;
    private final VariableResolver variableResolver;
    private Attributes[] attributesSeq = new Attributes[20];

    public InvocationContext(Supplier<PdfWriter> writer) throws IOException, DocumentException {
        this.fontAwesome = new FontAwesomeAdapter();
        this.processors = Maps.newHashMap();
        this.processorDefault = new DefaultProcessor();
        this.defaultFont = FontFactory.getFont(FontFactory.HELVETICA, 12.0f, Font.NORMAL);
        this.fontStack = new Stack<Font>();
        this.fontStack.push(defaultFont);
        this.tableStack = new Stack<TableInfos>();
        this.styleSheet = new FriendlyStyle();
        this.verbatimFont = inconsolata();
        this.cellStylerStack = new Stack<CellStyler>();
        this.pygments = new PygmentsAdapter(new Pygments(), styleSheet, verbatimFont, 10.0f);
        this.sections = new Sections(
                FontFactory.getFont(FontFactory.HELVETICA, 18.0f, Font.BOLD, BaseColor.BLACK),
                FontFactory.getFont(FontFactory.HELVETICA, 16.0f, Font.BOLD, BaseColor.DARK_GRAY),
                FontFactory.getFont(FontFactory.HELVETICA, 14.0f, Font.BOLD, BaseColor.DARK_GRAY)
        );
        this.variableResolver = new VariableResolver().declare("image-dir", "/");

        initProcessors(writer);
    }

    public Font verbatimFont(RGB rgb) {
        return verbatimFont(toColor(rgb));
    }

    private Font verbatimFont(BaseColor color) {
        return new Font(verbatimFont, 12, Font.NORMAL, color);
    }

    public Chunk symbol(String symbol, float size, BaseColor color) {
        return fontAwesome.symbol(symbol, size, color);
    }

    public List<Element> process(int depth, Node node) {
        Processor processor = processors.get(node.getClass());
        if (processor == null)
            processor = processorDefault;

        dumpProcessor(depth, node, processor);

        List<Element> elements = processor.process(depth, node, this);
        if (depth == 0) {
            return rebuildChapterSectionTree(elements);
        } else {
            return elements;
        }
    }

    private List<Element> rebuildChapterSectionTree(List<Element> elements) {
        List<Element> tree = Lists.newArrayList();
        Section prev = null;
        for (Element element : elements) {
            if (element instanceof Section) {
                //
                // Chapter are not added to the document
                // but section are automatically added on parent section...
                //
                if (element instanceof Chapter)
                    tree.add(element);
                prev = (Section) element;
            } else {
                if (prev != null)
                    prev.add(element);
                else
                    tree.add(element);
            }
        }
        return tree;
    }

    private void dumpProcessor(int depth, Node node, Processor processor) {
        if (log.isDebugEnabled()) {
            String indent = indent(depth);

            StringBuilder out = new StringBuilder();
            out.append(indent);
            if (processor == processorDefault) {
                out.append(" ");
            } else {
                out.append("*");
            }
            out.append(node);

            if (node instanceof HeaderNode) {
                out.append(" L:").append(((HeaderNode) node).getLevel());
            }
            if (node instanceof VerbatimNode) {
                out.append(" T:").append(((VerbatimNode) node).getType());
            }
            if (node instanceof RefImageNode) {
                RefImageNode rn = (RefImageNode) node;
                out.append(" separatorSpace: '").append(rn.separatorSpace).append("' refKey: '").append(rn.referenceKey).append("'");
            }

            log.debug(out.toString());
        }
    }

    private static String indent(int level) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < level; i++)
            b.append("    ");
        return b.toString();
    }

    public List<Element> processChildren(int level, Node node) {
        List<Element> subs = Lists.newArrayList();
        for (Node child : node.getChildren()) {
            // attributes apply on siblings :)
            Attributes attributes = consumeAttributes(level + 1);
            List<Element> elements = process(level + 1, child);
            applyAttributes(elements, attributes);
            subs.addAll(elements);
        }
        return subs;
    }

    private void applyAttributes(List<Element> elements, Attributes attributes) {
        for (Element e : elements)
            applyAttributes(e, attributes);
    }

    private void applyAttributes(Element e, Attributes attributes) {
        String align = attributes.getString("align");
        if ("center".equalsIgnoreCase(align)) {
            if (e instanceof Image) {
                ((Image) e).setAlignment(Image.MIDDLE);
            } else if (e instanceof Paragraph) {
                ((Paragraph) e).setAlignment(Element.ALIGN_CENTER);
            }
        }
    }

    protected void initProcessors(Supplier<PdfWriter> writer) {
        processors.put(SimpleNode.class, new SimpleNodeProcessor());
        processors.put(BlockQuoteNode.class, new BlockQuoteNodeProcessor());
        processors.put(ParaNode.class, new ParaNodeProcessor());
        processors.put(VerbatimNode.class, new VerbatimNodeProcessor(pygments, new VerbatimDitaaExtension(pygments, writer)));
        processors.put(TextNode.class, new TextNodeProcessor());
        processors.put(SpecialTextNode.class, new SpecialTextNodeProcessor());
        processors.put(OrderedListNode.class, new OrderedListNodeProcessor());
        processors.put(BulletListNode.class, new BulletListNodeProcessor());
        processors.put(ListItemNode.class, new ListItemNodeProcessor());
        processors.put(HeaderNode.class, new HeaderNodeProcessor(sections));
        processors.put(CodeNode.class, new CodeNodeProcessor(
                verbatimFont(styleSheet.foregroundOf(Token.Text)),
                toColor(styleSheet.backgroundColor())
        ));
        processors.put(StrongEmphSuperNode.class, new StrongEmphSuperNodeProcessor());
        processors.put(StrikeNode.class, new StrikeNodeProcessor());
        processors.put(SuperNode.class, new SuperNodeProcessor());

        BaseColor veryLightGray = new BaseColor(230, 230, 230);
        processors.put(TableNode.class, new TableNodeProcessor(new AlternateTableRowBackground(veryLightGray)));
        Font headerFont = copyFont(defaultFont).bold().color(BaseColor.WHITE).get();
        processors.put(TableHeaderNode.class, new TableHeaderNodeProcessor(new CellStyler(headerFont) {
            @Override
            public void applyStyle(PdfPCell cell) {
                cell.setBackgroundColor(BaseColor.BLACK);
            }
        }));
        processors.put(TableBodyNode.class, new TableBodyNodeProcessor(new CellStyler(defaultFont)));
        processors.put(TableRowNode.class, new TableRowNodeProcessor());
        processors.put(TableCellNode.class, new TableCellNodeProcessor());

        processors.put(ExpImageNode.class, new ExpImageNodeProcessor(variableResolver));
        processors.put(RefImageNode.class, new RefImageNodeProcessor(variableResolver));
        processors.put(AttributesNode.class, new AttributesNodeProcessor());
    }

    public Font peekFont() {
        return fontStack.peek();
    }

    public void pushFont(Font font) {
        fontStack.push(font);
    }

    public Font popFont() {
        return fontStack.pop();
    }

    public TableInfos peekTable() {
        return tableStack.peek();
    }

    public void pushTable(TableInfos table) {
        tableStack.push(table);
    }

    public TableInfos popTable() {
        return tableStack.pop();
    }

    public CellStyler peekCellStyler() {
        return cellStylerStack.peek();
    }

    public void pushCellStyler(CellStyler cellStyler) {
        cellStylerStack.push(cellStyler);
    }

    public CellStyler popCellStyler() {
        return cellStylerStack.pop();
    }

    public void pushAttributes(int level, Attributes attributes) {
        if (level >= attributesSeq.length) {
            Attributes[] increased = new Attributes[level + 5];
            System.arraycopy(attributesSeq, 0, increased, 0, attributesSeq.length);
            attributesSeq = increased;
        }

        fill(attributesSeq, level, attributesSeq.length, null);
        attributesSeq[level] = attributes;
    }

    @SuppressWarnings("unchecked")
    public Attributes peekAttributes(int level) {
        return peekAttributes(level, false);
    }

    public Attributes peekAttributes(int level, boolean lookupForAncestor) {
        if (level >= attributesSeq.length || attributesSeq[level] == null) {
            if (lookupForAncestor) {
                for (int i = level; i >= 0; i--) {
                    if (attributesSeq[i] != null)
                        return attributesSeq[i];
                }
            }

            // still there, nothing found in ancestor tree
            return new Attributes();
        }
        return attributesSeq[level];
    }

    @SuppressWarnings("unchecked")
    private Attributes consumeAttributes(int level) {
        Attributes map = peekAttributes(level);
        if (level < attributesSeq.length)
            fill(attributesSeq, level, attributesSeq.length, null);
        return map;
    }
}
