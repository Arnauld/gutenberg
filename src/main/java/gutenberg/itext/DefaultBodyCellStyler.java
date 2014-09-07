package gutenberg.itext;

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
    public Font cellFont() {
        return styles.getFont(Styles.TABLE_BODY_FONT).get();
    }
}
