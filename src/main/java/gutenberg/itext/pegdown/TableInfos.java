package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import org.pegdown.ast.TableColumnNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableInfos {
    private final PdfPTable table;
    private final List<TableColumnNode> tableNodeColumns;

    public TableInfos(PdfPTable table, List<TableColumnNode> tableNodeColumns) {
        this.table = table;
        this.tableNodeColumns = tableNodeColumns;
    }

    public PdfPTable getTable() {
        return table;
    }

    public int columnAlignment(int col) {
        TableColumnNode tcn = tableNodeColumns.get(col);
        switch (tcn.getAlignment()) {
            case Left:
                return Element.ALIGN_LEFT;
            case Right:
                return Element.ALIGN_RIGHT;
            case None:
                return Element.ALIGN_UNDEFINED;
            case Center:
            default:
                return Element.ALIGN_CENTER;
        }
    }
}
