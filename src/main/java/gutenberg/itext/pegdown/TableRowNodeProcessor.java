package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableRowNodeProcessor extends Processor {
    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
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

        // elements already added
        return elements();
    }
}
