package gutenberg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RGB {

    public static RGB rgb(String stringPresentation) {
        Pattern p = Pattern.compile("^#?([0-9a-f]{1,2})([0-9a-f]{1,2})([0-9a-f]{1,2})$");
        Matcher matcher = p.matcher(stringPresentation);
        if (!matcher.matches())
            throw new IllegalArgumentException("");
        int r = parseHex(matcher.group(1));
        int g = parseHex(matcher.group(2));
        int b = parseHex(matcher.group(3));
        return new RGB(r, g, b);
    }

    private static int parseHex(String s) {
        if (s.length() == 1)
            s = s + s;
        return Integer.parseInt(s, 16);
    }

    private final int value;

    public RGB(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public RGB(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);
    }

    /**
     * Returns the red component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the red component.
     */
    public int r() {
        return (rgba() >> 16) & 0xFF;
    }

    /**
     * Returns the green component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the green component.
     */
    public int g() {
        return (rgba() >> 8) & 0xFF;
    }

    /**
     * Returns the blue component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the blue component.
     */
    public int b() {
        return (rgba() >> 0) & 0xFF;
    }

    /**
     * Returns the alpha component in the range 0-255.
     *
     * @return the alpha component.
     */
    public int a() {
        return (rgba() >> 24) & 0xff;
    }

    /**
     * Returns the RGB value representing the color in the default sRGB
     * {@link java.awt.image.ColorModel}.
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
     *
     * @return the RGB value of the color in the default sRGB
     */
    public int rgba() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RGB rgb = (RGB) o;
        return rgb.value == value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "RGB{" + value + "}";
    }
}
