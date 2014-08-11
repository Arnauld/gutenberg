package gutenberg.itext;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CellStyler {
    private final Font font;

    public CellStyler(Font font) {
        this.font = font;
    }

    public Font cellFont() {
        return font;
    }

    public void applyStyle(PdfPCell cell) {
    }
}
