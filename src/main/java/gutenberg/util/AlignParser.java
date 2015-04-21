package gutenberg.util;

/**
 *
 * Created by aloyer060113 on 21/04/2015.
 */
public class AlignParser {
    public Align parse(String input) throws AlignFormatException {
        String cleaned = input.toLowerCase().trim();
        if (cleaned.equalsIgnoreCase("right"))
            return Align.Right;
        else if (cleaned.equalsIgnoreCase("center"))
            return Align.Center;
        else if (cleaned.equalsIgnoreCase("left"))
            return Align.Left;
        else
            throw new AlignFormatException("Unknown alignment mode '" + input + "'");
    }
}
