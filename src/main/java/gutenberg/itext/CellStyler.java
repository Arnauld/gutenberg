package gutenberg.itext;

import com.google.common.base.Supplier;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CellStyler {
    private final Supplier<Font> font;

    public CellStyler(Supplier<Font> font) {
        this.font = font;
    }

    public Font cellFont() {
        return font.get();
    }

    public PdfPCell applyStyle(PdfPCell cell) {
        return cell;
    }
}
