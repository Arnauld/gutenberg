package gutenberg.pegdown.plugin;

import gutenberg.pegdown.AbstractPegdownTest;
import org.junit.Test;
import org.parboiled.common.Reference;
import org.pegdown.Extensions;
import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericBoxPluginTest extends AbstractPegdownTest {

    @Test
    public void genericBox_simpleCase_ast_build() throws IOException {
        String mkd = loadResource("genericBoxText.md").trim();
        String ast = loadResource("genericBoxText.ast").trim();
        check(null, mkd, ast);
    }

    @Test
    public void generixBox_with_attributes_ast_build() throws IOException {
        String mkd = loadResource("genericBox-with-attributesText.md").trim();
        String ast = loadResource("genericBox-with-attributesText.ast").trim();
        PegDownPlugins.Builder plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class);
        check(plugins, mkd, ast);
    }

    private void check(PegDownPlugins.Builder extraPluginsBuilder, String markdown, String expectedAst) {
        Reference<Parser> parserRef = new Reference<Parser>();

        PegDownPlugins plugins =
                (extraPluginsBuilder == null ? PegDownPlugins.builder() : extraPluginsBuilder)
                        .withPlugin(GenericBoxPlugin.class, parserRef)
                        .build();

        // ---

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        parserRef.set(processor.parser);

        RootNode rootNode = processor.parseMarkdown(normalize(markdown).toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(expectedAst));
    }

}