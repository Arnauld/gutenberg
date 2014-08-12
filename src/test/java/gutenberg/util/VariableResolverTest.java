package gutenberg.util;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableResolverTest {

    private VariableResolver resolver;

    @Before
    public void setUp() {
        resolver = new VariableResolver();
    }

    @Test
    public void simpleCase() {
        assertThat(resolver.resolve("<m2dir>/repository")).isEqualTo("<m2dir>/repository");

        resolver.declare("m2dir", "/home/.m2");
        assertThat(resolver.resolve("<m2dir>/repository")).isEqualTo("/home/.m2/repository");
    }
}