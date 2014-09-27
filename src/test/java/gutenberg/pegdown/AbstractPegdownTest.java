package gutenberg.pegdown;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.parboiled.Rule;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ToStringFormatter;
import org.parboiled.trees.GraphUtils;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

import java.io.IOException;
import java.io.InputStream;

import static org.parboiled.support.ParseTreeUtils.printNodeTree;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Ignore("Base class for test")
public class AbstractPegdownTest {

    protected String loadResource(String resourceName) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourceName);
        try {
            return IOUtils.toString(stream, "utf8");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    protected String normalize(String string) {
        return string.replace("\r\n", "\n").replace("\r", "\n");
    }

    protected String dumpAST(RootNode rootNode) {
        return GraphUtils.printTree(rootNode, new ToStringFormatter<Node>());
    }

    protected boolean trace = false;

    @SuppressWarnings("unchecked")
    protected <T> T parse(Rule rule, String input) {
        ParsingResult<?> result;
        if (trace) {
            result = new TracingParseRunner<Object>(rule).run(input);
        } else {
            result = new ReportingParseRunner(rule).run(input);
        }

        if (!result.parseErrors.isEmpty()) {
            for (ParseError parseError : result.parseErrors) {
                System.out.println(ErrorUtils.printParseError(parseError));
            }
        } else if (trace) {
            System.out.println(printNodeTree(result) + '\n');
        }

        return (T) result.valueStack.pop();
    }
}
