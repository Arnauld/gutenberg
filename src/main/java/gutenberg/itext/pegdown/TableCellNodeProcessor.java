package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import gutenberg.itext.CellStyler;
import gutenberg.pegdown.TreeNavigation;
import org.pegdown.ast.Node;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableHeaderNode;
import org.pegdown.ast.TableRowNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableCellNodeProcessor extends Processor {
    @SuppressWarnings("unchecked")
    @Override
    public void process(int level, Node node, InvocationContext context) {
        TreeNavigation nav = context.treeNavigation();
        boolean isHeaderCell = nav.ancestorTreeMatches(TableCellNode.class, TableRowNode.class, TableHeaderNode.class);

        CellStyler cellStyler = context.peekCellStyler();
        context.pushFont(cellStyler.cellFont());
        List<Element> elements = context.collectChildren(level, node);
        context.popFont();

        Phrase phrase = new Phrase();
        phrase.addAll(elements);

        int colspan = ((TableCellNode) node).getColSpan();

        PdfPCell cell = isHeaderCell ? headerCell(phrase) : new PdfPCell(phrase);
        cell.setColspan(colspan);
        cellStyler.applyStyle(cell);

        context.append(cell);
    }

    private PdfPHeaderCell headerCell(Phrase phrase) {
        PdfPHeaderCell headerCell  = new PdfPHeaderCell();
        headerCell.setPhrase(phrase);
        return headerCell;
    }
}
