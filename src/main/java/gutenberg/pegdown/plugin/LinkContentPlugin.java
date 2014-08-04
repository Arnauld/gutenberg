package gutenberg.pegdown.plugin;

import org.parboiled.Rule;
import org.parboiled.common.Reference;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.plugins.BlockPluginParser;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LinkContentPlugin extends Parser implements BlockPluginParser {

    public final Reference<Parser> delegate;

    public LinkContentPlugin(Reference<Parser> delegate) {
        super(ALL, 1000l, DefaultParseRunnerProvider);
        this.delegate = delegate;
    }

    @Override
    public Rule[] blockPluginRules() {
        return new Rule[]{FootnoteContent()};
    }

    public Rule FootnoteContent() {
        StringBuilderVar ref = new StringBuilderVar();
        StringBuilderVar txt = new StringBuilderVar();
        return NodeSequence(
                '[',
                OneOrMore(TestNot(']'), ANY, ref.append(matchedChar())),
                ']', ':', Sp(),
                ZeroOrMore(TestNot(BlankLine()), ANY, txt.append(matchedChar())),
                FirstOf(BlankLine(), EOI),
                push(new LinkContentNode(ref.getString(), txt.getString())));
    }


}
