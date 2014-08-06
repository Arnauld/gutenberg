package gutenberg.pygments;

import gutenberg.util.RGB;

import static gutenberg.util.RGB.rgb;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Style {

    private static final int NONE = 0;
    private static final int BOLD = 1 << 1;
    private static final int ITALIC = 1 << 2;
    private final RGB foreground;
    private final RGB background;
    private final int flags;

    public Style() {
        this(null, null, NONE);
    }

    private Style(RGB foreground, RGB background, int flags) {
        this.foreground = foreground;
        this.background = background;
        this.flags = flags;
    }

    public static Style style() {
        return new Style();
    }

    public Style bold() {
        return new Style(foreground, background, flags + BOLD);
    }

    public Style italic() {
        return new Style(foreground, background, flags + ITALIC);
    }

    public Style fg(RGB color) {
        return new Style(color, background, flags);
    }

    public Style fg(String color) {
        return fg(rgb(color));
    }

    public Style bg(RGB color) {
        return new Style(foreground, color, flags);
    }

    public Style bg(String color) {
        return bg(rgb(color));
    }

    public Style combine(Style other) {
        RGB fg = other.foreground != null ? other.foreground : foreground;
        RGB bg = other.background != null ? other.background : background;
        return new Style(fg, bg, flags + other.flags);
    }

    public RGB fg() {
        return foreground;
    }

    public RGB bg() {
        return background;
    }

    public boolean isBold() {
        return (flags & BOLD) == BOLD;
    }

    public boolean isItalic() {
        return (flags & ITALIC) == ITALIC;
    }
}
