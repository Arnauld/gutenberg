package gutenberg.util;

import java.util.TreeMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RomanNumeral {

    final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "m");
        map.put(900, "cm");
        map.put(500, "d");
        map.put(400, "cd");
        map.put(100, "c");
        map.put(90, "xc");
        map.put(50, "l");
        map.put(40, "xl");
        map.put(10, "x");
        map.put(9, "ix");
        map.put(5, "v");
        map.put(4, "iv");
        map.put(1, "i");

    }

    public String format(int value) {
        if (value <= 0)
            throw new IllegalArgumentException("Negative number not allowed: " + value);
        return raw(value);
    }

    private String raw(int value) {
        if (value < 0)
            throw new IllegalArgumentException("Negative number not allowed: " + value);

        int l = map.floorKey(value);
        if (value == l) {
            return map.get(value);
        }
        return map.get(l) + raw(value - l);
    }
}
