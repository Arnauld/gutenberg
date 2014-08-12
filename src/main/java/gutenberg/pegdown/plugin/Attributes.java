package gutenberg.pegdown.plugin;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Attributes {

    private Pattern keyValue = Pattern.compile("([a-zA-Z_\\-]+)" + "\\s*=\\s*" + "(\"[^\"]+\"|[^\",]+)");
    private final Map<String, String> map;

    public Attributes() {
        this.map = Maps.newHashMap();
    }

    public Attributes declare(String key, String value) {
        map.put(key.toLowerCase(), value);
        return this;
    }

    public Attributes appendCommaSeparatedKeyValuePairs(String text) {
        Matcher matcher = keyValue.matcher(text);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = unquote(matcher.group(2));
            map.put(key, value);
        }
        return this;
    }

    private String unquote(String group) {
        if (group.startsWith("\""))
            return group.substring(1, group.length() - 1);
        else
            return group;
    }

    public String getString(String key) {
        return map.get(key);
    }

    public boolean isOn(String key) {
        String val = map.get(key);
        if (val == null)
            return false;
        else
            return val.equalsIgnoreCase("on") || val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("true");
    }

    public boolean isOff(String key) {
        String val = map.get(key);
        if (val == null)
            return false;
        else
            return val.equalsIgnoreCase("off") || val.equalsIgnoreCase("no") || val.equalsIgnoreCase("false");
    }
}
