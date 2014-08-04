package gutenberg.pegdown;

import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PegdownTest extends AbstractPegdownTest {

    @Test
    public void dumpAST() throws IOException {
        String mkd = loadResource("astText.md").trim();
        String ast = loadResource("astText.ast").trim();

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(ast));
    }
}
