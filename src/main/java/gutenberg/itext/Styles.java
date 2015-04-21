package gutenberg.itext;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;
import java.util.Map;

import static com.itextpdf.text.FontFactory.HELVETICA;
import static gutenberg.itext.FontDescriptor.fontDescriptor;
import static gutenberg.itext.ITextUtils.inconsolata;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Styles {
    public static final String DEFAULT_COLOR = "default-color";
    public static final String DEFAULT_FONT = "default-font";
    //
    public static final String CODE_FONT = "code-font";
    //
    public static final String INLINE_CODE_FONT = "inline-code-font";
    public static final String INLINE_CODE_BACKGROUND = "inline-code-background";
    //
    public static final String H1_FONT = "H1-font";
    public static final String H2_FONT = "H2-font";
    public static final String H3_FONT = "H3-font";
    public static final String H4_FONT = "H4-font";
    //
    public static final String TABLE_ALTERNATE_BACKGROUND = "table-alternate-background";
    public static final String TABLE_HEADER_FONT = "table-header-font";
    public static final String TABLE_HEADER_BACKGROUD = "table-header-background";
    public static final String TABLE_BODY_FONT = "table-body-font";
    public static final String TABLE_BODY_CELL_BORDER_COLOR = "table-body-cell-border";
    //
    public static final String BLOCKQUOTE_COLOR = "blockquote-color";

    //

    private Map<Object, FontModifier> registeredFontModifiers = Maps.newConcurrentMap();
    private Map<Object, FontDescriptor> registeredFonts = Maps.newConcurrentMap();
    private Map<Object, BaseColor> registeredColors = Maps.newConcurrentMap();

    public Styles initDefaults() {
        FontDescriptor defaultFont = fontDescriptor(defaultFontName(), defaultFontSize(), Font.NORMAL, BaseColor.BLACK);

        registeredColors.put(DEFAULT_COLOR, BaseColor.BLACK);
        registeredFonts.put(DEFAULT_FONT, defaultFont);

        registeredFonts.put(CODE_FONT, fontDescriptor(verbatimBaseFont(), defaultFontSize(), Font.NORMAL, BaseColor.BLACK));
        registeredFonts.put(INLINE_CODE_FONT, fontDescriptor(verbatimBaseFont(), defaultFontSize(), Font.NORMAL, BaseColor.BLACK));
        registeredColors.put(INLINE_CODE_BACKGROUND, Colors.VERY2_LIGHT_GRAY);

        registeredFonts.put(H1_FONT, fontDescriptor(defaultFontName(), 18.0f, Font.BOLD, BaseColor.BLACK));
        registeredFonts.put(H2_FONT, fontDescriptor(defaultFontName(), 16.0f, Font.BOLD, BaseColor.DARK_GRAY));
        registeredFonts.put(H3_FONT, fontDescriptor(defaultFontName(), 14.0f, Font.BOLD, BaseColor.DARK_GRAY));
        registeredFonts.put(H4_FONT, fontDescriptor(defaultFontName(), 14.0f, Font.ITALIC, BaseColor.DARK_GRAY));

        registeredColors.put(TABLE_ALTERNATE_BACKGROUND, Colors.VERY2_LIGHT_GRAY);
        registeredFonts.put(TABLE_HEADER_FONT, fontDescriptor(defaultFontName(), 14.0f, Font.ITALIC, BaseColor.WHITE));
        registeredColors.put(TABLE_HEADER_BACKGROUD, BaseColor.BLACK);
        registeredFonts.put(TABLE_BODY_FONT, defaultFont);
        registeredColors.put(TABLE_BODY_CELL_BORDER_COLOR, Colors.LIGHT_GRAY);

        registeredColors.put(BLOCKQUOTE_COLOR, Colors.LIGHT_GRAY);

        return this;
    }

    protected BaseFont verbatimBaseFont() {
        try {
            return inconsolata();
        } catch (IOException e) {
            return FontFactory.getFont(FontFactory.COURIER).getBaseFont();
        } catch (DocumentException e) {
            return FontFactory.getFont(FontFactory.COURIER).getBaseFont();
        }
    }

    public BaseColor defaultColor() {
        return getColor(DEFAULT_COLOR).or(BaseColor.BLACK);
    }

    public Font defaultFont() {
        return FontFactory.getFont(defaultFontName(), defaultFontSize(), Font.NORMAL);
    }

    public float defaultFontSize() {
        return 10.0f;
    }

    protected String defaultFontName() {
        return HELVETICA;
    }

    public Font sectionTitleFontForLevel(int hLevel) {
        FontDescriptor descriptor = null;
        for (int i = 0; i < hLevel; i++) {
            FontDescriptor d = registeredFonts.get("H" + hLevel + "-font");
            if (d != null) {
                descriptor = d;
            }
        }
        return descriptor == null ? defaultFont() : descriptor.font();
    }

    public Optional<Font> getFont(Object key) {
        FontDescriptor fontDescriptor = registeredFonts.get(key);
        FontModifier modifier = registeredFontModifiers.get(key);
        if (modifier == null)
            modifier = FontModifier.NULL_MODIFIER;

        if (fontDescriptor == null)
            return Optional.absent();
        else
            return Optional.of(modifier.apply(fontDescriptor.font()));
    }

    public Optional<Font> getFont(Object key, int style, BaseColor color) {
        FontDescriptor fontDescriptor = registeredFonts.get(key);
        FontModifier modifier = registeredFontModifiers.get(key);
        if (modifier == null)
            modifier = FontModifier.NULL_MODIFIER;

        if (fontDescriptor == null)
            return Optional.absent();
        else
            return Optional.of(modifier.apply(fontDescriptor.font(style, color)));
    }

    public Font getFontOrDefault(Object key) {
        Optional<Font> fontOpt = getFont(key);
        if (fontOpt.isPresent())
            return fontOpt.get();
        else
            return defaultFont();
    }

    public Font getFontOrDefault(Object key, int style, BaseColor color) {
        Optional<Font> fontOpt = getFont(key, style, color);
        if (fontOpt.isPresent())
            return fontOpt.get();
        else
            return new FontCopier(defaultFont()).style(style).color(color).get();
    }

    public void registerFont(Object key, Font font) {
        registeredFonts.put(key, fontDescriptor(font));
    }

    public void registerFontModifier(Object key, FontModifier modifier) {
        registeredFontModifiers.put(key, modifier);
    }

    public Optional<BaseColor> getColor(Object key) {
        BaseColor color = registeredColors.get(key);
        if (color == null)
            return Optional.absent();
        else
            return Optional.of(color);
    }

    public BaseColor getColorOrDefault(Object key) {
        Optional<BaseColor> color = getColor(key);
        if (color.isPresent())
            return color.get();
        else
            return defaultColor();
    }

    public void registerColor(Object key, BaseColor color) {
        registeredColors.put(key, color);
    }

}
