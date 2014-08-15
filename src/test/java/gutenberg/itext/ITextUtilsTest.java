package gutenberg.itext;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ITextUtilsTest {

    @Test
    public void toColor_should_return_null_if_null_is_provided() {
        assertThat(ITextUtils.toColor(null)).isNull();
    }
}