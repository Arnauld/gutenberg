package gutenberg.itext;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DefaultHeaderCellStyler extends CellStyler {

    private final Styles styles;

    public DefaultHeaderCellStyler(Styles styles) {
        super(null);
        this.styles = styles;
    }

    @Override
    public PdfPCell applyStyle(PdfPCell cell) {
        cell.setBackgroundColor(styles.getColor(Styles.TABLE_HEADER_BACKGROUD).get());
        return cell;
    }

    @Override
    public Font cellFont() {
        return styles.getFont(Styles.TABLE_HEADER_FONT).get();
    }
}
