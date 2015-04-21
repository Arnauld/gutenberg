package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@SuppressWarnings("UnusedDeclaration")
public class FontCopier extends FontModifier {

    public static FontCopier copyFont(Font font) {
        return new FontCopier(font);
    }

    private final Font font;

    public FontCopier(Font font) {
        this.font = font;
    }

    public FontCopier style(int style) {
        super.style(style);
        return this;
    }

    public FontCopier size(float size) {
        super.size(size);
        return this;
    }

    public FontCopier color(BaseColor color) {
        super.color(color);
        return this;
    }

    public Font get() {
        return apply(font);
    }

    public FontCopier bold() {
        super.bold();
        return this;
    }

    public FontCopier noBold() {
        super.noBold();
        return this;
    }

    public FontCopier italic() {
        super.italic();
        return this;
    }

    public FontCopier noItalic() {
        super.noItalic();
        return this;
    }
}
