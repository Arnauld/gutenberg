package gutenberg.pygments;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LexerInfo {
    private final String name;
    private final List<String> aliases;

    public LexerInfo(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public String toString() {
        return "LexerInfo{'" + name + "\': " + aliases + '}';
    }
}
