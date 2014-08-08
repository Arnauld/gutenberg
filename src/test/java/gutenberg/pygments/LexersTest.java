package gutenberg.pygments;

import com.google.common.base.Optional;
import org.junit.Test;
import org.python.core.PyObject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LexersTest {

    private PyGateway gateway = new PyGateway();

    @Test
    public void lookup_with_known_alias__java() {
        Lexers lexers = new Lexers();
        Optional<Object> found = lexers.lookupLexer(gateway, "java");
        assertThat(found).isNotNull();
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isInstanceOf(PyObject.class);
        PyObject actual = (PyObject) found.get();
        assertThat(actual.getType().toString()).isEqualTo("<class 'pygments.lexers.jvm.JavaLexer'>");
    }

    @Test
    public void lookup_with_unknown_alias__Kaboom() {
        Lexers lexers = new Lexers();
        Optional<Object> found = lexers.lookupLexer(gateway, "kaboom");
        assertThat(found).isNotNull();
        assertThat(found.isPresent()).isFalse();
    }

    @Test
    public void loadAvailableLexers() {
        Lexers lexers = new Lexers();
        List<LexerInfo> lexerInfos = lexers.loadAvailableLexers(gateway);
        System.out.println("" + lexerInfos);
    }
}