package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@SuppressWarnings("UnusedDeclaration")
public class FontCopier {

    public static FontCopier copyFont(Font font) {
        return new FontCopier(font);
    }

    private final Font font;
    private BaseColor color;
    private Integer style;
    private Float size;

    public FontCopier(Font font) {
        this.font = font;
    }

    public FontCopier style(int style) {
        this.style = style;
        return this;
    }

    public FontCopier size(float size) {
        this.size = size;
        return this;
    }

    public FontCopier color(BaseColor color) {
        this.color = color;
        return this;
    }

    public Font get() {
        return new Font(font.getBaseFont(),
                val(size, font.getSize()),
                val(style, font.getStyle()),
                val(color, font.getColor()));
    }

    private static <T> T val(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public FontCopier bold() {
        ensureStyleIsDefined();
        style = style | Font.BOLD;
        return this;
    }

    public FontCopier noBold() {
        ensureStyleIsDefined();
        style = style & ~Font.BOLD;
        return this;
    }

    public FontCopier italic() {
        ensureStyleIsDefined();
        style = style | Font.ITALIC;
        return this;
    }

    public FontCopier noItalic() {
        ensureStyleIsDefined();
        style = style & ~Font.ITALIC;
        return this;
    }

    private void ensureStyleIsDefined() {
        if (style == null) {
            style = ((font.getStyle() != Font.UNDEFINED) ? font.getStyle() : Font.NORMAL);
        }
    }
}
