package gutenberg.itext.pegdown;

import com.google.common.base.Optional;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.ITextUtils;
import gutenberg.itext.Styles;
import gutenberg.pegdown.TreeNavigation;
import gutenberg.pegdown.plugin.AttributesNode;
import gutenberg.util.Attributes;
import gutenberg.util.RGB;
import gutenberg.util.RGBFormatException;
import org.pegdown.ast.Node;

import java.util.List;

import static gutenberg.pegdown.TreeNavigation.ofType;
import static gutenberg.pegdown.TreeNavigation.siblingBefore;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GenericBoxNodeProcessor extends Processor {


    @Override
    public void process(int level, Node node, InvocationContext context) {
        Attributes attrs = lookupAttributes(context);

        List<Element> subs = context.collectChildren(level, node);

        Paragraph p = new Paragraph();
        p.addAll(subs);

        PdfPCell cell = new PdfPCell();
        cell.addElement(p);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);

        Styles styles = context.iTextContext().styles();
        BaseColor symbolColor = symbolColor(attrs, styles);
        PdfPCell cellSymbol = new PdfPCell(
                new Phrase(context.symbol(symbol(attrs), 24, symbolColor))
        );
        cellSymbol.setVerticalAlignment(Element.ALIGN_TOP);
        cellSymbol.setBorderColor(BaseColor.LIGHT_GRAY);
        cellSymbol.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        cellSymbol.setPaddingTop(10f);
        cellSymbol.setPaddingBottom(10f);
        cellSymbol.setPaddingLeft(0f);
        cellSymbol.setPaddingRight(10f);

        PdfPTable table = new PdfPTable(new float[]{1, 10});
        table.addCell(cellSymbol);
        table.addCell(cell);
        table.setSpacingBefore(20f);
        table.setSpacingAfter(20f);
        ITextUtils.applyAttributes(table, attrs);

        context.append(table);

    }

    private BaseColor symbolColor(Attributes attrs, Styles styles) {
        BaseColor color = null;
        try {
            RGB rgb = attrs.getRGB("icon-color");
            if (rgb != null)
                color = new BaseColor(rgb.r(), rgb.g(), rgb.b());
        } catch (RGBFormatException e) {
            log.warn("Invalid color definition for icon", e);
        }
        if (color == null)
            color = styles.getColor(Styles.GENERIC_SYMBOL_COLOR).or(BaseColor.BLACK);
        return color;
    }

    private String symbol(Attributes attrs) {
        String icon = attrs.getString("icon");
        if (icon == null)
            return "question";
        return icon;
    }

    private Attributes lookupAttributes(InvocationContext context) {
        TreeNavigation nav = context.treeNavigation();
        Optional<TreeNavigation> attrNode =
                siblingBefore()
                        .then(ofType(AttributesNode.class))
                        .query(nav);

        Attributes attributes;
        if (attrNode.isPresent()) {
            attributes = attrNode.get().peek(AttributesNode.class).asAttributes();
        } else {
            attributes = new Attributes();
        }
        return attributes;
    }
}
