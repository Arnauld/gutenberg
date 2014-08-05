package gutenberg.font;

import org.junit.Test;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class FontAwesomeTest {

    @Test
    public void pattern_should_be_able_to_decode_variables() {
        decode("@fa-var-adjust: \"\\f042\";", "adjust", "f042");
    }

    private void decode(String input, String name, String code) {
        Pattern pattern = FontAwesome.variablePattern();
        assertThat(input).matches(pattern);
        Matcher matcher = pattern.matcher(input);
        assertThat(matcher.matches()).isTrue();
        assertThat(matcher.group(1)).isEqualTo(name);
        assertThat(matcher.group(2)).isEqualTo(code);

    }

    @Test
    public void variablesFile_should_be_correctly_read() {
        FontAwesome font = new FontAwesome().loadVariablesFromResources("/font/variables.less");
        assertThat(font.get("youtube-square")).isEqualTo(String.valueOf((char)0xf166));
        assertThat(font.keys()).hasSize(503);
    }

}