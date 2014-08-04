package gutenberg.pegdown.plugin;

import org.parboiled.Rule;
import org.parboiled.common.Reference;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.plugins.BlockPluginParser;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GenericBoxPlugin extends Parser implements BlockPluginParser {

    public final Reference<Parser> delegate;

    public GenericBoxPlugin(Reference<Parser> delegate) {
        super(ALL, 1000l, DefaultParseRunnerProvider);
        this.delegate = delegate;
    }

    @Override
    public Rule[] blockPluginRules() {
        return new Rule[]{Box()};
    }

    public Rule Box() {
        StringBuilderVar inner = new StringBuilderVar();
        return NodeSequence(
                OneOrMore(
                        xcrossedOut(Sequence('G', '>', Optional(' ')), inner),
                        Line(inner)
                ),
                // trigger a recursive parsing run on the inner source we just built
                // and attach the root of the inner parses AST
                push(new GenericBoxNode(
                        xwithIndicesShifted(
                                delegate.get().parseInternal(inner),
                                (Integer) peek()
                        ).getChildren()))
        );
    }

    Rule xcrossedOut(Rule rule, StringBuilderVar block) {
        return Sequence(rule, xappendCrossed(block));
    }

    boolean xappendCrossed(StringBuilderVar block) {
        for (int i = 0; i < matchLength(); i++) {
            block.append(CROSSED_OUT);
        }
        return true;
    }

    Node xwithIndicesShifted(Node node, int delta) {
        ((AbstractNode) node).shiftIndices(delta);
        for (Node subNode : node.getChildren()) {
            xwithIndicesShifted(subNode, delta);
        }
        return node;
    }
}
