package gutenberg.pygments;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TokenWithValue {
    public final Token token;
    public final String value;

    public TokenWithValue(Token token, String value) {
        this.token = token;
        this.value = value;
    }
}
