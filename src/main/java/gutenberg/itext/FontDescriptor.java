package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

import static gutenberg.itext.FontCopier.copyFont;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FontDescriptor {

    public static FontDescriptor fontDescriptor(Font font) {
        return new FontDescriptor(font, font.getBaseFont(), null, font.getSize(), font.getStyle(), font.getColor());
    }

    public static FontDescriptor fontDescriptor(String fontName, float size, int style, BaseColor color) {
        return new FontDescriptor(null, null, fontName, size, style, color);
    }

    public static FontDescriptor fontDescriptor(BaseFont baseFont, float size, int style, BaseColor color) {
        return new FontDescriptor(null, baseFont, null, size, style, color);
    }

    private Font font;
    private final BaseFont baseFont;
    private final String fontName;
    private final int style;
    private final BaseColor color;
    private final float size;

    public FontDescriptor(Font font, BaseFont baseFont, String fontName, float size, int style, BaseColor color) {
        this.font = font;
        this.baseFont = baseFont;
        this.fontName = fontName;
        this.style = style;
        this.color = color;
        this.size = size;
    }

    public BaseFont baseFont() {
        return font().getBaseFont();
    }

    public Font font() {
        if (font == null) {
            if (baseFont != null)
                font = new Font(baseFont, size, style, color);
            else
                font = FontFactory.getFont(fontName, size, style, color);
        }
        return font;
    }

    public Font font(int style, BaseColor color) {
        return copyFont(font()).style(style).color(color).get();
    }
}
