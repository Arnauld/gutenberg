package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FontModifier {

    public static FontModifier NULL_MODIFIER = new FontModifier().freeze();

    public static FontModifier fontModifier() {
        return new FontModifier();
    }

    private boolean frozen = false;
    private Boolean bold;
    private Boolean italic;
    private Integer style;
    private Float size;
    private BaseColor color;

    public FontModifier style(int style) {
        checkFrozen();
        this.style = style;
        return this;
    }

    public FontModifier size(float size) {
        checkFrozen();
        this.size = size;
        return this;
    }

    public FontModifier color(BaseColor color) {
        checkFrozen();
        this.color = color;
        return this;
    }

    public FontModifier bold() {
        checkFrozen();
        bold = true;
        return this;
    }

    public FontModifier noBold() {
        checkFrozen();
        bold = false;
        return this;
    }

    public FontModifier italic() {
        checkFrozen();
        italic = true;
        return this;
    }

    public FontModifier noItalic() {
        checkFrozen();
        italic = false;
        return this;
    }

    public FontModifier freeze() {
        frozen = true;
        return this;
    }

    private void checkFrozen() {
        if (frozen)
            throw new IllegalStateException("Modifier frozen!");
    }

    public Font apply(Font font) {
        if (bold == null
                && italic == null
                && style == null
                && color == null
                && size == null)
            return font;

        int fontStyle = styleOf(font);
        fontStyle = overrideIfNotNull(fontStyle, style);
        fontStyle = applyModifier(fontStyle, bold, Font.BOLD);
        fontStyle = applyModifier(fontStyle, italic, Font.ITALIC);

        return new Font(font.getBaseFont(),
                val(size, font.getSize()),
                val(fontStyle, font.getStyle()),
                val(color, font.getColor()));
    }

    private static int applyModifier(int fontStyle, Boolean mode, int flag) {
        if (mode == null) {
            return fontStyle;
        }
        if (mode) {
            return fontStyle | flag;
        } else {
            return fontStyle & ~flag;
        }
    }

    private static int overrideIfNotNull(int fontStyle, Integer overrider) {
        if (overrider != null) {
            return overrider;
        }
        return fontStyle;
    }

    private static <T> T val(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    private static int styleOf(Font font) {
        return ((font.getStyle() != Font.UNDEFINED) ? font.getStyle() : Font.NORMAL);
    }
}
