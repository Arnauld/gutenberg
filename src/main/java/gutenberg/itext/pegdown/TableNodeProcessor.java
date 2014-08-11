package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import org.pegdown.ast.Node;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableNodeProcessor extends Processor {

    private final PdfPTableEvent[] tableEvents;

    public TableNodeProcessor(PdfPTableEvent... tableEvents) {
        this.tableEvents = tableEvents;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {

        TableNode tableNode = (TableNode) node;
        List<TableColumnNode> tableNodeColumns = tableNode.getColumns();

        PdfPTable table = new PdfPTable(tableNodeColumns.size());
        for (PdfPTableEvent tableEvent : tableEvents) {
            table.setTableEvent(tableEvent);
        }
        context.pushTable(new TableInfos(table, tableNodeColumns));
        context.processChildren(level, node);
        context.popTable();
        return elements(table);
    }
}
