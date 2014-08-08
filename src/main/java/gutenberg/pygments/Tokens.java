package gutenberg.pygments;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Tokens implements Iterable<TokenWithValue> {
    private List<TokenWithValue> tokens = Lists.newArrayList();

    public Tokens append(Token token, String value) {
        tokens.add(new TokenWithValue(token, value));
        return this;
    }

    @Override
    public Iterator<TokenWithValue> iterator() {
        return tokens.iterator();
    }

    @Override
    public String toString() {
        return tokens.toString();
    }
}
