package gutenberg.itext.model;

import com.itextpdf.text.Font;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RichText {
    private final String text;
    private final Font font;

    public RichText(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }
}
