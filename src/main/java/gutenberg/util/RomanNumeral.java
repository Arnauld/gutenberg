package gutenberg.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RomanNumeral {
    public String format(int value) {
        if (value <= 0)
            throw new IllegalArgumentException("Negative number not allowed: " + value);
        return raw(value);
    }

    private String raw(int value) {
        if (value < 0)
            throw new IllegalArgumentException("Negative number not allowed: " + value);

        switch (value) {
            case 0:
                return "";
            case 1:
                return "i";
            case 5:
                return "v";
            case 10:
                return "x";
            default:
                if (value <= 5 - 2) {
                    return raw(value - 1) + raw(1);
                }
                if (value <= 5) {
                    return raw(5 - value) + raw(5);
                }
                if (value <= 10 - 2) {
                    return raw(5) + raw(value - 5);
                } else if (value <= 10) {
                    return raw(10 - value) + raw(10);
                } else {
                    return raw(10) + raw(value - 10);
                }
        }
    }
}
