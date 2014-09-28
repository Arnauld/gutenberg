package gutenberg.itext.pegdown;

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

    public static final Object TABLE_SPACING_BEFORE = "table-spacing-before";
    public static final Object TABLE_SPACING_AFTER = "table-spacing-after";

    private final PdfPTableEvent[] tableEvents;

    public TableNodeProcessor(PdfPTableEvent... tableEvents) {
        this.tableEvents = tableEvents;
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {

        TableNode tableNode = (TableNode) node;
        List<TableColumnNode> tableNodeColumns = tableNode.getColumns();

        PdfPTable table = new PdfPTable(tableNodeColumns.size());
        for (PdfPTableEvent tableEvent : tableEvents) {
            table.setTableEvent(tableEvent);
        }
        context.pushTable(new TableInfos(table, tableNodeColumns));
        context.processChildren(level, node);
        context.popTable();

        Float spacingBefore = context.iTextContext().<Float>getNullable(TABLE_SPACING_BEFORE).or(5f);
        Float spacingAfter = context.iTextContext().<Float>getNullable(TABLE_SPACING_AFTER).or(5f);
        table.setSpacingBefore(spacingBefore);
        table.setSpacingAfter(spacingAfter);
        context.append(table);
    }
}
