package gutenberg.itext.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SourceCodeTest {

    @Test
    public void toBytes_then_fromBytes_should_result_in_the_same_input() {
        SourceCode sourceCode = new SourceCode("java", "import java.util.*;");
        byte[] data = sourceCode.toBytes();
        sourceCode = SourceCode.fromBytes(data);

        assertThat(sourceCode).isNotNull();
        assertThat(sourceCode.lang()).isEqualTo("java");
        assertThat(sourceCode.content()).isEqualTo("import java.util.*;");
    }
}