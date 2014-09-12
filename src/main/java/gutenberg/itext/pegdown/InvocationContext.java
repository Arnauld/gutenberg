package gutenberg.itext.pegdown;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itextpdf.text.*;
import gutenberg.itext.*;
import gutenberg.pegdown.References;
import gutenberg.pegdown.TreeNavigation;
import gutenberg.pegdown.plugin.AttributesNode;
import gutenberg.util.Attributes;
import gutenberg.util.MutableSupplier;
import gutenberg.util.VariableResolver;
import org.pegdown.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static java.util.Arrays.fill;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InvocationContext {

    private final ITextContext iTextContext;
    private final Styles styles;
    private Logger log = LoggerFactory.getLogger(InvocationContext.class);

    private final Processor processorDefault;
    private final Stack<Font> fontStack;
    private final Stack<TableInfos> tableStack;
    private final FontAwesomeAdapter fontAwesome;
    private final Stack<CellStyler> cellStylerStack;
    private final TreeNavigation treeNavigation;
    // ---
    private Map<Class<?>, Processor> processors;
    private VariableResolver variableResolver;
    private Attributes[] attributesSeq = new Attributes[20];
    private References references;
    private MutableSupplier<Sections> sectionsSupplier;

    public InvocationContext(ITextContext iTextContext, Styles styles) throws IOException, DocumentException {
        this.iTextContext = iTextContext;
        this.styles = styles;
        // ---
        this.fontAwesome = new FontAwesomeAdapter();
        this.processorDefault = new DefaultProcessor();
        this.fontStack = new Stack<Font>();
        this.fontStack.push(styles.defaultFont());
        this.tableStack = new Stack<TableInfos>();
        this.cellStylerStack = new Stack<CellStyler>();
        //this.pygments = new PygmentsAdapter(new Pygments(), styleSheet, styles);
        this.sectionsSupplier = new MutableSupplier<Sections>(new Sections(styles));
        this.variableResolver = new VariableResolver().declare("image-dir", "/");
        this.treeNavigation = new TreeNavigation();
        this.references = new References();
    }

    public InvocationContext useSections(Sections sections) {
        this.sectionsSupplier.set(sections);
        return this;
    }

    public ITextContext iTextContext() {
        return iTextContext;
    }

    public References references() {
        return references;
    }

    public VariableResolver variableResolver() {
        return variableResolver;
    }

    public InvocationContext variableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
        return this;
    }

    public Chunk symbol(String symbol, float size, BaseColor color) {
        return fontAwesome.symbol(symbol, size, color);
    }

    public List<Element> process(int depth, Node node) {
        if (depth == 0) {
            references.traverse(node);
        }

        Processor processor = processors().get(node.getClass());
        if (processor == null)
            processor = processorDefault;

        treeNavigation.push(node);

        dumpProcessor(depth, node, processor);
        List<Element> elements = processor.process(depth, node, this);

        treeNavigation.pop();

        if (depth == 0) {
            return rebuildChapterSectionTree(elements);
        } else {
            return elements;
        }
    }

    private Map<Class<?>, Processor> processors() {
        if (processors == null) {
            processors = Maps.newHashMap();
            initProcessors(iTextContext, styles);
        }
        return processors;
    }

    public TreeNavigation treeNavigation() {
        return treeNavigation;
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
            List<Element> elements = process(level + 1, child);
            // attributes apply on siblings :)
            if (!(child instanceof AttributesNode)) {
                Attributes attributes = consumeAttributes(level + 1);
                applyAttributes(elements, attributes);
            }
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

    protected void initProcessors(ITextContext iTextContext, final Styles styles) {
        processors.put(SimpleNode.class, new SimpleNodeProcessor());
        processors.put(BlockQuoteNode.class, new BlockQuoteNodeProcessor(styles));
        processors.put(ParaNode.class, new ParaNodeProcessor());
        processors.put(VerbatimNode.class, new VerbatimNodeProcessor());
        processors.put(TextNode.class, new TextNodeProcessor());
        processors.put(SpecialTextNode.class, new SpecialTextNodeProcessor());
        processors.put(OrderedListNode.class, new OrderedListNodeProcessor());
        processors.put(BulletListNode.class, new BulletListNodeProcessor());
        processors.put(ListItemNode.class, new ListItemNodeProcessor());
        processors.put(HeaderNode.class, new HeaderNodeProcessor(sectionsSupplier));
        processors.put(CodeNode.class, new CodeNodeProcessor(styles));
        processors.put(StrongEmphSuperNode.class, new StrongEmphSuperNodeProcessor());
        processors.put(StrikeNode.class, new StrikeNodeProcessor());
        processors.put(SuperNode.class, new SuperNodeProcessor());
        processors.put(TableNode.class, new TableNodeProcessor(new AlternateTableRowBackground(styles)));
        processors.put(TableHeaderNode.class, new TableHeaderNodeProcessor(new DefaultHeaderCellStyler(styles)));
        processors.put(TableBodyNode.class, new TableBodyNodeProcessor(new DefaultBodyCellStyler(styles)));
        processors.put(TableRowNode.class, new TableRowNodeProcessor());
        processors.put(TableCellNode.class, new TableCellNodeProcessor());
        processors.put(ExpImageNode.class, new ExpImageNodeProcessor(variableResolver, iTextContext));
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
        log.debug(indent(level) + "Attributes pushed for level {}; attributes: {}", level, attributes);
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
        log.debug(indent(level) + "consuming attributes for level {}; attributes: {}", level, map);
        if (level < attributesSeq.length)
            fill(attributesSeq, level, attributesSeq.length, null);
        return map;
    }
}
