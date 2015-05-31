package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import gutenberg.font.FontAwesome;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FontAwesomeAdapter {

    // font/FontAwesome.otf appears twice bigger on windows...
    // webfont seems to look almost the same under windows and macos
    private static final String FONT_AWESOME_RESOURCE = "font/fontawesome-webfont.ttf";
    private final FontAwesome fontAwesome;
    private final BaseFont baseFont;

    public FontAwesomeAdapter() throws IOException, DocumentException {
        fontAwesome = FontAwesome.getInstance();
        baseFont = BaseFont.createFont(FONT_AWESOME_RESOURCE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    public Chunk symbol(String name, float size, BaseColor fg) {
        String s = fontAwesome.get(name);
        if(s==null)
            throw new IllegalArgumentException("Unrecognized symbol '" + name + "'");
        return new Chunk(s, new Font(baseFont, size, Font.NORMAL, fg));
    }

}
