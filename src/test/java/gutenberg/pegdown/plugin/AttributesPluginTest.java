package gutenberg.pegdown.plugin;

import gutenberg.pegdown.AbstractPegdownTest;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AttributesPluginTest extends AbstractPegdownTest {

    @Test
    public void astBuild_simpleCase() throws IOException {
        String mkd = loadResource("attributesText-simple.md").trim();
        String ast = loadResource("attributesText-simple.ast").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(ast));
    }

    @Test
    public void astBuild_advancedCase() throws IOException {
        String mkd = loadResource("attributesText.md").trim();
        String ast = loadResource("attributesText.ast").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(ast));
    }

}
