package gutenberg.pegdown.plugin;

import gutenberg.util.Attributes;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AttributesTest {

    @Test
    public void one_attribute__key_value() {
        Attributes map = new Attributes().appendCommaSeparatedKeyValuePairs("icon=automobile");
        assertThat(map.getString("icon")).isEqualTo("automobile");
    }

    @Test
    public void two_attributes() {
        Attributes map = new Attributes().appendCommaSeparatedKeyValuePairs("width=60%,float=left");
        assertThat(map.getString("width")).isEqualTo("60%");
        assertThat(map.getString("float")).isEqualTo("left");
    }

    @Test
    public void two_attributes__one_with_quotes() {
        Attributes map = new Attributes().appendCommaSeparatedKeyValuePairs("width=\"60%\",float=left");
        assertThat(map.getString("width")).isEqualTo("60%");
        assertThat(map.getString("float")).isEqualTo("left");
    }


}