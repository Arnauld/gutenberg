package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.pegdown.TreeNavigation;
import org.pegdown.ast.Node;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableHeaderNode;
import org.pegdown.ast.TableRowNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableRowNodeProcessor extends Processor {
    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        TreeNavigation nav = context.treeNavigation();
        boolean isHeaderRow = nav.ancestorTreeMatches(TableRowNode.class, TableHeaderNode.class);

        List<Element> elements = context.processChildren(level, node);

        TableInfos tableInfos = context.peekTable();
        PdfPTable table = tableInfos.getTable();
        int col = 0;
        for (Element element : elements) {
            PdfPCell cell = (PdfPCell)element;
            cell.setHorizontalAlignment(tableInfos.columnAlignment(col));
            table.addCell(cell);

            col += cell.getColspan();
        }
        table.completeRow();

        if(isHeaderRow) {
            int headerRows = table.getHeaderRows();
            table.setHeaderRows(headerRows + 1);
        }

        // elements already added
        return elements();
    }
}
