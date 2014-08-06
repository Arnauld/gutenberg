package gutenberg.util;

import gutenberg.util.RGB;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class RGBTest {
    @Test
    @Parameters(method = "rgbValidValues")
    public void parse_valid_input(String input, int r, int g, int b) {
        RGB rgb = RGB.rgb(input);
        assertThat(rgb.r()).isEqualTo(r);
        assertThat(rgb.g()).isEqualTo(g);
        assertThat(rgb.b()).isEqualTo(b);
    }

    @SuppressWarnings("UnusedDeclaration")
    private Object[] rgbValidValues() {
        return $(
                $("#f8f8f2", 0xf8, 0xf8, 0xf2),
                $("f8f8f2", 0xf8, 0xf8, 0xf2),
                $("#ae3", 0xaa, 0xee, 0x33),
                $("ae3", 0xaa, 0xee, 0x33)
        );
    }

    public static Object[] $(Object... params) {
        return params;
    }
}