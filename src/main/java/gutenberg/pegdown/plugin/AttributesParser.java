package gutenberg.pegdown.plugin;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@BuildParseTree
public class AttributesParser extends Parser {

    public AttributesParser() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }

    public Rule attributesRules() {
        return Sequence(
                Sp(),
                "{",
                push(new AttributeListNode()),
                CommaSeparatedAttributes(),
                "}",
                Sp(),
                FirstOf(Newline(), EOI)
        );
    }

    public Rule CommaSeparatedAttributes() {
        return Sequence(
                Attribute(),
                ((AttributeListNode) peek(1)).append((AttributeNode) pop()),
                ZeroOrMore(
                        Sequence(
                                ",", Attribute(),
                                ((AttributeListNode) peek(1)).append((AttributeNode) pop())
                        )
                )
        );
    }

    @SuppressSubnodes
    public Rule Attribute() {
        return NodeSequence(
                Name(),
                "=",
                Value(),
                push(new AttributeNode((String) pop(1), (String) pop())));
    }

    @SuppressSubnodes
    public Rule Name() {
        StringBuilderVar text = new StringBuilderVar();
        return Sequence(FirstOf(
                        Sequence(Ch('"'), OneOrMore(TestNot('"'), BaseParser.ANY, text.append(matchedChar())), Ch('"')),
                        OneOrMore(TestNot("="), TestNot(Newline()), BaseParser.ANY, text.append(matchedChar()))),
                push(text.getString())
        );
    }

    @SuppressSubnodes
    public Rule Value() {
        StringBuilderVar text = new StringBuilderVar();
        return Sequence(FirstOf(
                        Sequence(Ch('"'), OneOrMore(TestNot('"'), BaseParser.ANY, text.append(matchedChar())), Ch('"')),
                        OneOrMore(TestNot("}"), TestNot(","), TestNot(Newline()), BaseParser.ANY, text.append(matchedChar()))),
                push(text.getString())
        );
    }
}
