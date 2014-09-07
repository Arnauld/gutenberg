package gutenberg.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

import static gutenberg.itext.ITextUtils.inconsolata;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Styles {
    public static final String DEFAULT_FONT = "default-font";

    public BaseFont verbatimBaseFont() {
        try {
            return inconsolata();
        } catch (IOException e) {
            return FontFactory.getFont(FontFactory.COURIER).getBaseFont();
        } catch (DocumentException e) {
            return FontFactory.getFont(FontFactory.COURIER).getBaseFont();
        }
    }

    public Font defaultFont() {
        return FontFactory.getFont(defaultFontName(), defaultFontSize(), Font.NORMAL);
    }

    public float defaultFontSize() {
        return 12.0f;
    }

    public String defaultFontName() {
        return FontFactory.HELVETICA;
    }
}
