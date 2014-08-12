package gutenberg.itext;

import com.google.common.base.Suppliers;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PegdownPdfTest {

    private String workingDir;

    @Before
    public void setUp() throws IOException, DocumentException {
        workingDir = new TestSettings().workingDir();
    }

    @Test
    public void astText() throws Exception {
        process("astText", "/gutenberg/pegdown/astText.md");
    }

    @Test
    public void allInOne() throws Exception {
        process("allInOne", "/gutenberg/pegdown/allInOne.md");
    }

    @Test
    public void headers() throws Exception {
        process("headers", "/gutenberg/pegdown/headers.md");
    }

    @Test
    public void headers_01() throws Exception {
        process("headers_01", "/gutenberg/pegdown/headers-01.md");
    }

    @Test
    public void list_01() throws Exception {
        process("list_01", "/gutenberg/pegdown/list-01.md");
    }

    @Test
    public void table_01() throws Exception {
        process("table_01", "/gutenberg/pegdown/table-01.md");
    }

    @Test
    public void image_01() throws Exception {
        process("image_01", "/gutenberg/pegdown/image-01.md");
    }

    @Test
    public void code_01() throws Exception {
        process("code_01", "/gutenberg/pegdown/code-01.md");
    }

    @Test
    public void code_02_w_attributes() throws Exception {
        process("code_02_w_attributes", "/gutenberg/pegdown/code-02-w-attributes.md");
    }

    private void process(String usecase, String resourcePath) throws Exception {
        ITextContext iTextContext = openDocument(usecase);
        Document document = iTextContext.getDocument();
        String mkd = loadResource(resourcePath).trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        InvocationContext context = new InvocationContext(Suppliers.ofInstance(iTextContext.getPdfWriter()));

        List<Element> elements = context.process(0, rootNode);
        for (Element element : elements) {
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
