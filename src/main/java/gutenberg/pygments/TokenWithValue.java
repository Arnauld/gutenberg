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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenWithValue that = (TokenWithValue) o;
        return token == that.token && value.equals(that.value);

    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TokenWithValue{" + token + ", '" + value + '\'' + '}';
    }
}
