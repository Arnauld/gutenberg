package gutenberg.pegdown.plugin;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.plugins.BlockPluginParser;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AttributesPlugin extends Parser implements BlockPluginParser {

    public AttributesPlugin() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }

    @Override
    public Rule[] blockPluginRules() {
        return new Rule[]{attributesRules()};
    }

    public Rule attributesRules() {
        StringBuilderVar text = new StringBuilderVar();
        return NodeSequence(
                Sp(),
                "{",
                OneOrMore(TestNot("}"), TestNot(Newline()), BaseParser.ANY, text.append(matchedChar())),
                "}",
                Sp(),
                Newline(),
                push(new AttributesNode(text.getString()))
        );
    }
}
