package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import gutenberg.util.Dimension;
import gutenberg.util.RGB;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ITextUtils {
    public static BaseColor toColor(RGB fg) {
        if (fg != null) {
            return new BaseColor(fg.r(), fg.g(), fg.b());
        }
        return null;
    }

    public static BaseFont inconsolata() throws IOException, DocumentException {
        return createEmbeddedFont("font/Inconsolata.otf", BaseFont.WINANSI);
    }

    public static BaseFont createEmbeddedFont(String fontName, String encoding) throws IOException, DocumentException {
        return BaseFont.createFont(fontName, encoding, BaseFont.EMBEDDED);
    }

    public static void adjustOrScaleToFit(Image img, Dimension dim, Rectangle box) {
        if (dim == null) {
            scaleToFit(img, box);
            return;
        }

        float width = img.getWidth();
        switch (dim.unit()) {
            case Percent:
                width = box.getWidth() * dim.amount() / 100f;
                break;
            case Px:
                width = dim.amount();
                break;
        }

        // W --> w
        // H --> h  •••> h = w * H / W
        float height = width * img.getHeight() / img.getWidth();
        img.scaleAbsolute(width, height);
    }

    public static void scaleToFit(Image img, Rectangle box) {
        float scaleWidth = box.getWidth() / img.getWidth();
        float scaleHeight = box.getHeight() / img.getHeight();
        float scale = Math.min(scaleHeight, scaleWidth);
        if (scale < 1)
            img.scalePercent(scale * 100f);
    }
}
