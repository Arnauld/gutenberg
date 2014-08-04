package gutenberg.pegdown.plugin;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.InRange;
import gutenberg.pegdown.AbstractPegdownTest;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Theories.class)
public class ExtensionsCompatibilityPluginTest extends AbstractPegdownTest {

    @Theory
    public void attributesPlugin(@ForAll @InRange(minInt = 0, maxInt = 0x0000FFFF) int extensions) throws IOException {
        String mkd = loadResource("attributesText-simple.md").trim();
        String ast = loadResource("attributesText-simple.ast").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();

        PegDownProcessor processor = new PegDownProcessor(extensions, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(ast));
    }
}
