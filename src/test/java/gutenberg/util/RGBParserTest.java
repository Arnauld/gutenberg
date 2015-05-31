package gutenberg.util;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RGBParserTest {

    @Test
    public void should_parse_hex_colors() throws RGBFormatException {
        RGB rgb = new RGBParser().parse("#FF0012");
        assertThat(rgb.r()).isEqualTo(0xFF);
        assertThat(rgb.g()).isEqualTo(0x00);
        assertThat(rgb.b()).isEqualTo(0x12);
    }

    @Test(expected = RGBFormatException.class)
    public void should_fail_with_invalid_format__missing_digit() throws RGBFormatException {
        new RGBParser().parse("#FF002");
    }

    @Test(expected = RGBFormatException.class)
    public void should_fail_with_invalid_format__missing_digits() throws RGBFormatException {
        new RGBParser().parse("#FF00");
    }

    @Test(expected = RGBFormatException.class)
    public void should_fail_with_invalid_format__missing_sharp() throws RGBFormatException {
        new RGBParser().parse("02FF00");
    }

    @Test
    public void should_parse_predefined_colors() throws RGBFormatException {
        for (String str : Arrays.asList("red", "dark-red", "green", "blue", "yellow", "gray", "light-gray"))
            new RGBParser().parse(str);
    }
}