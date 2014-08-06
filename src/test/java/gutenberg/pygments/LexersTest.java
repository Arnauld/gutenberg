package gutenberg.pygments;

import com.google.common.base.Optional;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LexersTest {

    @Test
    public void lookup_with_known_alias__java() {
        Lexers lexers = new Lexers();
        Optional<Object> found = lexers.lookupLexer("java");
        assertThat(found).isNotNull();
        assertThat(found.isPresent()).isTrue();
    }

    @Test
    public void lookup_with_unknown_alias__Kaboom() {
        Lexers lexers = new Lexers();
        Optional<Object> found = lexers.lookupLexer("kaboom");
        assertThat(found).isNotNull();
        assertThat(found.isPresent()).isFalse();
    }

    @Test
    public void loadAvailableLexers() {
        Lexers lexers = new Lexers();
        lexers.loadAvailableLexers();
    }
}