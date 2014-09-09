package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DefaultBodyCellStyler extends CellStyler {
    private final Styles styles;

    public DefaultBodyCellStyler(Styles styles) {
        super(null);
        this.styles = styles;
    }

    @Override
    public PdfPCell applyStyle(PdfPCell cell) {
        cell.setBorderColor(styles.getColor(Styles.TABLE_BODY_CELL_BORDER_COLOR).or(BaseColor.BLACK));
        return cell;
    }

    @Override
    public Font cellFont() {
        return styles.getFont(Styles.TABLE_BODY_FONT).get();
    }
}
