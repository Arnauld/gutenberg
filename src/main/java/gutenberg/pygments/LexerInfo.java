package gutenberg.pygments;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LexerInfo {
    private final String name;
    private final List<String> aliases;

    public LexerInfo(String name, String... aliases) {
        this(name, Arrays.asList(aliases));
    }

    public LexerInfo(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LexerInfo info = (LexerInfo) o;
        return aliases.equals(info.aliases) && name.equals(info.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + aliases.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LexerInfo{'" + name + "\': " + aliases + '}';
    }
}
