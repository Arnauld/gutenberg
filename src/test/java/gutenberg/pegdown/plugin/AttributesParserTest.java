package gutenberg.pegdown.plugin;

import gutenberg.pegdown.AbstractPegdownTest;
import org.junit.Test;
import org.parboiled.Parboiled;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AttributesParserTest extends AbstractPegdownTest {

    @Test
    public void usecase() {
        String input = "{language=ruby,line-numbers=off}";
        AttributesParser parser = Parboiled.createParser(AttributesParser.class);
        AttributeListNode list = parse(parser.attributesRules(), input);

        List<AttributeNode> attributes = list.getAttributes();
        assertThat(attributes).hasSize(2);
        assertThat(attributes.get(0).getKey()).isEqualTo("language");
        assertThat(attributes.get(0).getValue()).isEqualTo("ruby");
        assertThat(attributes.get(1).getKey()).isEqualTo("line-numbers");
        assertThat(attributes.get(1).getValue()).isEqualTo("off");
    }


}