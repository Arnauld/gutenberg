package gutenberg.util;

import static gutenberg.util.RGB.rgb;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Style {

    public enum Italic {
        True,
        False,
        Inherit
    }

    public enum Bold {
        True,
        False,
        Inherit
    }

    public enum Strikethrough {
        True,
        False,
        Inherit
    }

    public enum Underline {
        True,
        False,
        Inherit
    }

    public static Style style() {
        return new Style();
    }

    private final RGB foreground;
    private final RGB background;
    private final Italic italic;
    private final Bold bold;
    private final Underline underline;
    private final Strikethrough strikethrough;

    public Style() {
        this(null, null, Italic.Inherit, Bold.Inherit, Underline.Inherit, Strikethrough.Inherit);
    }

    private Style(RGB foreground,
                  RGB background,
                  Italic italic,
                  Bold bold,
                  Underline underline,
                  Strikethrough strikethrough) {
        this.foreground = foreground;
        this.background = background;
        this.italic = italic;
        this.bold = bold;
        this.underline = underline;
        this.strikethrough = strikethrough;
    }

    public Style bold() {
        return new Style(foreground, background, italic, Bold.True, underline, strikethrough);
    }

    public Style noBold() {
        return new Style(foreground, background, italic, Bold.False, underline, strikethrough);
    }

    public boolean isBold() {
        return bold == Bold.True;
    }

    public Style italic() {
        return new Style(foreground, background, Italic.True, bold, underline, strikethrough);
    }

    public Style noItalic() {
        return new Style(foreground, background, Italic.False, bold, underline, strikethrough);
    }

    public boolean isItalic() {
        return italic == Italic.True;
    }

    public Style underline() {
        return new Style(foreground, background, italic, bold, Underline.True, strikethrough);
    }

    public Style noUnderline() {
        return new Style(foreground, background, italic, bold, Underline.False, strikethrough);
    }

    public boolean isUnderline() {
        return underline == Underline.True;
    }

    public Style strikethrough() {
        return new Style(foreground, background, italic, bold, underline, Strikethrough.True);

    }

    public Style noStrikethrough() {
        return new Style(foreground, background, italic, bold, underline, Strikethrough.False);
    }

    public boolean isStrikethrough() {
        return strikethrough == Strikethrough.True;
    }

    public Style fg(RGB color) {
        return new Style(color, background, italic, bold, underline, strikethrough);
    }

    public Style fg(String color) {
        return fg(rgb(color));
    }

    public RGB fg() {
        return foreground;
    }

    public Style bg(RGB color) {
        return new Style(foreground, color, italic, bold, underline, strikethrough);
    }

    public Style bg(String color) {
        return bg(rgb(color));
    }

    public RGB bg() {
        return background;
    }

    public Style overrides(Style other) {
        RGB fg = other.foreground != null ? other.foreground : foreground;
        RGB bg = other.background != null ? other.background : background;
        return new Style(fg, bg,
                other.italic == Italic.Inherit ? italic : other.italic,
                other.bold == Bold.Inherit ? bold : other.bold,
                other.underline == Underline.Inherit ? underline : other.underline,
                other.strikethrough == Strikethrough.Inherit ? strikethrough : other.strikethrough
        );
    }

}
