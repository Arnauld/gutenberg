package gutenberg.pygments;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenTest {

    @Test
    public void path() {
        assertThat(Token.Keyword.path()).containsExactly(Token.Token, Token.Keyword);
        assertThat(Token.NumberIntegerLong.path()).containsExactly(Token.Token, Token.Literal, Token.Number, Token.NumberInteger, Token.NumberIntegerLong);
    }

    @Test
    public void repr() {
        assertThat(Token.Keyword.repr()).isEqualTo("TokenKeyword");
        assertThat(Token.String.repr()).isEqualTo("TokenLiteralString");
        assertThat(Token.NumberIntegerLong.repr()).isEqualTo("TokenLiteralNumberIntegerLong");
    }
}