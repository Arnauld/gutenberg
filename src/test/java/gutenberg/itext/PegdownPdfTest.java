package gutenberg.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import gutenberg.TestSettings;
import gutenberg.itext.pegdown.InvocationContext;
import gutenberg.pegdown.plugin.AttributesPlugin;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PegdownPdfTest {
    private static Logger log = LoggerFactory.getLogger(PegdownPdfTest.class);

    private String workingDir;
    private ITextContext iTextContext;
    private Document document;


    @Before
    public void setUp() throws IOException, DocumentException {
        workingDir = new TestSettings().workingDir();
    }

    @Test
    public void astText() throws Exception {
        iTextContext = openDocument("astText");
        document = iTextContext.getDocument();
        String mkd = loadResource("/gutenberg/pegdown/astText.md").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        InvocationContext context = new InvocationContext();
        for (Element element : context.process(0, rootNode)) {
            document.add(element);
        }
        iTextContext.close();
    }

    @Test
    public void allInOne() throws Exception {
        iTextContext = openDocument("allInOne");
        document = iTextContext.getDocument();
        String mkd = loadResource("/gutenberg/pegdown/allInOne.md").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        InvocationContext context = new InvocationContext();
        for (Element element : context.process(0, rootNode)) {
            document.add(element);
        }
        iTextContext.close();
    }


    private ITextContext openDocument(String method) throws FileNotFoundException, DocumentException {
        File file = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");
        return new ITextContext().open(file);
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
