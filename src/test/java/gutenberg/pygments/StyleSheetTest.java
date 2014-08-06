package gutenberg.pygments;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StyleSheetTest {

    @Test
    public void fallback() {
        MonokaiStyle stylesheet = new MonokaiStyle();

        assertStyle(stylesheet.styleOf(Token.Name), new Style().fg("#f8f8f2"));
        assertStyle(stylesheet.styleOf(Token.NameAttribute), new Style().fg("#a6e22e"));
        assertStyle(stylesheet.styleOf(Token.NameBuiltin), new Style().fg("#f8f8f2"));

        assertStyle(stylesheet.styleOf(Token.Generic), new Style().fg("#f8f8f2"));
        assertStyle(stylesheet.styleOf(Token.GenericEmph), new Style().fg("#f8f8f2").italic());
    }

    private void assertStyle(Style actual, Style expected) {
        assertThat(actual.fg()).isEqualTo(expected.fg());
        assertThat(actual.bg()).isEqualTo(expected.bg());
        assertThat(actual.isBold()).isEqualTo(expected.isBold());
        assertThat(actual.isItalic()).isEqualTo(expected.isItalic());
    }
}