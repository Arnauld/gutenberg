package gutenberg.itext.pegdown;

import com.google.common.base.Optional;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import gutenberg.pegdown.TreeNavigation;
import gutenberg.pegdown.plugin.AttributesNode;
import gutenberg.util.Attributes;
import gutenberg.util.Dimension;
import gutenberg.util.DimensionFormatException;
import gutenberg.util.KeyValues;
import org.pegdown.ast.Node;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableNode;

import java.util.List;

import static gutenberg.pegdown.TreeNavigation.*;

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


        KeyValues kvs = context.iTextContext().keyValues();

        Float spacingBefore = kvs.<Float>getNullable(TABLE_SPACING_BEFORE).or(5f);
        Float spacingAfter = kvs.<Float>getNullable(TABLE_SPACING_AFTER).or(5f);
        table.setSpacingBefore(spacingBefore);
        table.setSpacingAfter(spacingAfter);

        applyAttributes(context, table);
        context.append(table);
    }

    private void applyAttributes(InvocationContext context, PdfPTable table) {
        Attributes attributes = lookupAttributes(context);
        Dimension width = readWidth(attributes);
        if (width != null) {
            switch (width.unit()) {
                case Percent:
                    table.setWidthPercentage(width.amount());
                    break;
                case Px:
                default:
                    table.setTotalWidth(width.amount());
                    break;
            }
        }
    }

    private Attributes lookupAttributes(InvocationContext context) {
        TreeNavigation nav = context.treeNavigation();
        Optional<TreeNavigation> attrNode =
                firstAncestorOfType(TableNode.class)
                        .then(siblingBefore())
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

    private Dimension readWidth(Attributes attributes) {
        try {
            return attributes.getDimension("width");
        } catch (DimensionFormatException e) {
            log.warn("Unreadable width {}", attributes.getString("width"));
            return null;
        }
    }
}
