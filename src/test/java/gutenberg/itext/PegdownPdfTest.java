package gutenberg.itext;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import gutenberg.TestSettings;
import gutenberg.itext.pegdown.InvocationContext;
import gutenberg.pegdown.plugin.AttributesPlugin;
import gutenberg.util.VariableResolver;
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

import static org.assertj.core.api.Assertions.fail;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PegdownPdfTest {

    private String workingDir;
    private String projectDir;

    @Before
    public void setUp() throws IOException, DocumentException {
        TestSettings testSettings = new TestSettings();
        workingDir = testSettings.workingDir();
        projectDir = testSettings.projectDir();
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
    public void image_02_base_and_resource_dir() throws Exception {
        process("image_02_base_dir", "/gutenberg/pegdown/image-02-basedir.md", imagePathAndDirVariableResolver());
    }

    @Test
    public void image_03_attributes() throws Exception {
        process("image_03_attributes", "/gutenberg/pegdown/image-03-attributes.md", imagePathAndDirVariableResolver());
    }

    @Test
    public void image_04_references() throws Exception {
        process("image_04_ref", "/gutenberg/pegdown/image-04-ref.md", imagePathAndDirVariableResolver());
    }

    private Function<InvocationContext, InvocationContext> imagePathAndDirVariableResolver() {
        return new Function<InvocationContext, InvocationContext>() {
            @Override
            public InvocationContext apply(InvocationContext invocationContext) {
                VariableResolver variableResolver =
                        invocationContext
                                .variableResolver()
                                .declare("imageDir", "file://" + projectDir + "/doc")
                                .declare("resourcePathAsDir", "file://" + projectDir + "/src/test/resources")
                                .declare("resourcePath", "classpath:");
                return invocationContext.variableResolver(variableResolver);
            }
        };
    }

    @Test
    public void code_01() throws Exception {
        process("code_01", "/gutenberg/pegdown/code-01.md");
    }

    @Test
    public void code_02_w_attributes() throws Exception {
        process("code_02_w_attributes", "/gutenberg/pegdown/code-02-w-attributes.md");
    }

    @Test
    public void code_03_very_large_ditaa_attributes() throws Exception {
        process("code_03_very_large_ditaa", "/gutenberg/pegdown/code-03-very-large-ditaa.md");
    }

    @Test
    public void code_04_uml() throws Exception {
        process("code_04_uml", "/gutenberg/pegdown/code-04-uml.md");
    }

    @Test
    public void code_05_latex() throws Exception {
        process("code_05_latex", "/gutenberg/pegdown/code-05-latex-math.md");
    }

    private void process(String usecase, String resourcePath) throws Exception {
        process(usecase, resourcePath, Functions.<InvocationContext>identity());
    }

    private void process(String usecase, String resourcePath, Function<InvocationContext, InvocationContext> customizer) throws Exception {
        ITextContext iTextContext = openDocument(usecase);
        Document document = iTextContext.getDocument();
        String mkd = loadResource(resourcePath).trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        InvocationContext context = customizer.apply(new InvocationContext(iTextContext));
        if (context == null) {
            fail("No context");
            return;
        }

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
