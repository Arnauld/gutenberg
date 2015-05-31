package gutenberg.util;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RGBParser {

    private static final String PATTERN = "#[a-fA-F0-9]{6}";
    private static final Map<String, RGB> predefined = new MapBuilder<String, RGB>()
            .with("red", new RGB(255, 0, 0))
            .with("dark-red", new RGB(139, 0, 0))
            .with("black", new RGB(0, 0, 0))
            .with("pink", new RGB(255, 175, 175))
            .with("orange", new RGB(255, 200, 0))
            .with("yellow", new RGB(255, 255, 0))
            .with("green", new RGB(0, 255, 0))
            .with("light-gray", new RGB(192, 192, 192))
            .with("gray", new RGB(128, 128, 128))
            .with("dark-gray", new RGB(64, 64, 64))
            .with("magenta", new RGB(255, 0, 255))
            .with("cyan", new RGB(0, 255, 255))
            .with("blue", new RGB(0, 0, 255))
            .map();

    public RGB parse(String s) throws RGBFormatException {
        for (Map.Entry<String, RGB> entry : predefined.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(s.trim())) {
                return entry.getValue();
            }
        }

        if (s.trim().matches(PATTERN)) {
            int i = Integer.decode(s);
            return new RGB((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
        }

        throw new RGBFormatException("Neither match format /#hhhhhh/ nor predefined colors " + predefined.keySet() + "... got: '" + s + "'");
    }
}
