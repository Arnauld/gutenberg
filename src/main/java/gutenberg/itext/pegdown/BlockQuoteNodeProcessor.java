package gutenberg.itext.pegdown;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class BlockQuoteNodeProcessor extends Processor {

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        Font font = context.peekFont();
        context.pushFont(new Font(font.getBaseFont(), font.getSize(), font.getStyle() | Font.ITALIC, BaseColor.LIGHT_GRAY));
        List<Element> subs = context.processChildren(level, node);
        context.popFont();

        Paragraph p = new Paragraph();
        p.addAll(subs);

        PdfPCell cell = new PdfPCell();
        cell.addElement(p);
        cell.setBorder(Rectangle.NO_BORDER);

        PdfPCell cellSymbol = new PdfPCell(
                new Phrase(context.symbol("quote-left", 24, BaseColor.LIGHT_GRAY))
        );
        cellSymbol.setVerticalAlignment(Element.ALIGN_TOP);
        cellSymbol.setBorder(Rectangle.NO_BORDER);
        cellSymbol.setBorderWidthRight(1.0f);
        cellSymbol.setBorderColorRight(BaseColor.LIGHT_GRAY);
        cellSymbol.setPaddingTop(0f);
        cellSymbol.setPaddingBottom(5f);
        cellSymbol.setPaddingLeft(10f);
        cellSymbol.setPaddingRight(0f);

        PdfPTable table = new PdfPTable(new float[]{1, 10});
        table.addCell(cellSymbol);
        table.addCell(cell);
        table.setSpacingBefore(20f);
        table.setSpacingAfter(20f);

        return elements(table);
    }
}
