package gutenberg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DimensionParser {
    public Dimension parse(String input) throws DimensionFormatException {
        Pattern digits = Pattern.compile("(\\d+)([^\\d]+)");
        Matcher matcher = digits.matcher(input);
        if (!matcher.matches())
            throw new DimensionFormatException("Invalid input does not match <digits+><unit>");

        float amount = Float.parseFloat(matcher.group(1));
        String unitStr = matcher.group(2);
        Dimension.Unit unit;
        if (unitStr.equalsIgnoreCase("%"))
            unit = Dimension.Unit.Percent;
        else if (unitStr.equalsIgnoreCase("px"))
            unit = Dimension.Unit.Px;
        else
            throw new DimensionFormatException("Unknown dimension unit '" + unitStr + "'");

        return new Dimension(amount, unit);
    }
}
