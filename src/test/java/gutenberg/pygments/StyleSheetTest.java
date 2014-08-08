package gutenberg.pygments;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StyleSheetTest {

    @Test
    public void fallback() {
        StyleSheet stylesheet = new MonokaiStyle();

        assertStyle(stylesheet.styleOf(Token.Name), new Style().fg("#f8f8f2"));
        assertStyle(stylesheet.styleOf(Token.NameAttribute), new Style().fg("#a6e22e"));
        assertStyle(stylesheet.styleOf(Token.NameBuiltin), new Style().fg("#f8f8f2"));
        assertStyle(stylesheet.styleOf(Token.Generic), new Style().fg("#f8f8f2"));
        assertStyle(stylesheet.styleOf(Token.GenericEmph), new Style().fg("#f8f8f2").italic());
    }

    private void assertStyle(Style actual, Style expected) {
        assertThat(actual.fg()).describedAs("Foreground color").isEqualTo(expected.fg());
        assertThat(actual.bg()).describedAs("Background color").isEqualTo(expected.bg());
        assertThat(actual.isBold()).describedAs("Bold").isEqualTo(expected.isBold());
        assertThat(actual.isItalic()).describedAs("italic").isEqualTo(expected.isItalic());
    }
}