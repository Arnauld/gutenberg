package gutenberg.pegdown;

import gutenberg.pegdown.plugin.AttributesPlugin;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferencesTest {

    @Test
    public void usecase() throws IOException {
        RootNode rootNode = parseMarkdown("references-01.md");
        References references = new References();
        references.traverse(rootNode);

        References.Ref logo1 = references.lookup("logo1");
        assertThat(logo1).isNotNull();
        assertThat(logo1.url()).isEqualTo("${imageDir}/printing-history-gutenberg-press.jpg");

        References.Ref google = references.lookup("google");
        assertThat(google).isNotNull();
        assertThat(google.url()).isEqualTo("http://google.com/");
        assertThat(google.title()).isEqualTo("Google");
    }

    private RootNode parseMarkdown(String resourcePath) throws IOException {
        String mkd = loadResource(resourcePath).trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        return processor.parseMarkdown(mkd.toCharArray());
    }

    protected String loadResource(String resourceName) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourceName);
        try {
            return IOUtils.toString(stream, "utf8");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}