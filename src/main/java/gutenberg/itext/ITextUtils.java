package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
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
}
