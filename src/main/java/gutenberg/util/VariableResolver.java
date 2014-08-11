package gutenberg.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VariableResolver {
    private final Map<String, String> variables = Maps.newConcurrentMap();

    public VariableResolver declare(String key, String value) {
        variables.put(key, value);
        return this;
    }

    public String resolve(String input) {
        String fmt = input;
        for (String key : variables.keySet()) {
            fmt = fmt.replace("<" + key + ">", variables.get(key));
        }
        return fmt;
    }
}
