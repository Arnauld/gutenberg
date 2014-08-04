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

public class LinkContentPluginTest extends AbstractPegdownTest {

    @Test
    public void footnoteText_simpleCase_ast_build() throws IOException {
        String mkd = loadResource("linkContentText.md").trim();
        String ast = loadResource("linkContentText.ast").trim();
        check(mkd, ast);
    }

    private void check(String markdown, String expectedAst) {
        Reference<Parser> parserRef = new Reference<Parser>();

        PegDownPlugins plugins =
                PegDownPlugins.builder()
                        .withPlugin(LinkContentPlugin.class, parserRef)
                        .build();

        // ---

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        parserRef.set(processor.parser);

        RootNode rootNode = processor.parseMarkdown(markdown.toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(expectedAst));
    }

}