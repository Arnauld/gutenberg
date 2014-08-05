package gutenberg.font;

import com.google.common.collect.FluentIterable;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FontAwesome {
    public static FontAwesome getInstance() {
        return new FontAwesome().loadVariablesFromResources("/font/variables.less");
    }


    private Map<String, String> codeByMap = new ConcurrentHashMap<String, String>(512);

    protected static Pattern variablePattern() {
        // @fa-var-adjust: "\f042";
        return Pattern.compile("@fa\\-var\\-([^:]+):\\s+\"\\\\([^\"]+)\";");
    }

    protected FontAwesome loadVariablesFromResources(String resourcePath) {
        InputStream in = getClass().getResourceAsStream(resourcePath);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String line;
            Pattern variable = variablePattern();
            while ((line = reader.readLine()) != null) {
                Matcher matcher = variable.matcher(line);
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    String wat = matcher.group(2);
                    char character = (char) Integer.parseInt(wat, 16);
                    codeByMap.put(key, String.valueOf(character));
                }
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("We're in trouble!", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load variables definition for font", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return this;
    }

    public String get(String key) {
        return codeByMap.get(key);
    }

    public FluentIterable<String> keys() {
        return FluentIterable.from(codeByMap.keySet());
    }
}
