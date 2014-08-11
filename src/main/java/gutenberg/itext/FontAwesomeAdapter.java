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

    private final FontAwesome fontAwesome;
    private final BaseFont baseFont;

    public FontAwesomeAdapter() throws IOException, DocumentException {
        fontAwesome = FontAwesome.getInstance();
        baseFont = BaseFont.createFont("font/FontAwesome.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    public Chunk symbol(String name, float size, BaseColor fg) {
        String s = fontAwesome.get(name);
        return new Chunk(s, new Font(baseFont, size, Font.NORMAL, fg));
    }

}
