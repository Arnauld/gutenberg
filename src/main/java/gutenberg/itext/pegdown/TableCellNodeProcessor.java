package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import gutenberg.itext.CellStyler;
import org.pegdown.ast.Node;
import org.pegdown.ast.TableCellNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableCellNodeProcessor extends Processor {
    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {

        CellStyler cellStyler = context.peekCellStyler();
        context.pushFont(cellStyler.cellFont());
        List<Element> elements = context.processChildren(level, node);
        context.popFont();

        Phrase phrase = new Phrase();
        phrase.addAll(elements);

        int colspan = ((TableCellNode) node).getColSpan();

        PdfPCell cell = new PdfPCell(phrase);
        cell.setColspan(colspan);
        cellStyler.applyStyle(cell);

        return elements(cell);
    }
}
