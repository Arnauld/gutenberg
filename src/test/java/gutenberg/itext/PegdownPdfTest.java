package gutenberg.itext;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.itextpdf.text.DocumentException;
import gutenberg.TestSettings;
import gutenberg.itext.pegdown.InvocationContext;
import gutenberg.itext.support.ITextContextBuilder;
import gutenberg.pegdown.plugin.AttributesPlugin;
import gutenberg.pegdown.plugin.GenericBoxPlugin;
import gutenberg.pygments.styles.FriendlyStyle;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.parboiled.common.Reference;
import org.pegdown.Extensions;
import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PegdownPdfTest {

    private String workingDir;
    private String projectDir;
    private Styles styles;
    private File fileOut;

    @Before
    public void setUp() throws IOException, DocumentException {
        TestSettings testSettings = new TestSettings();
        workingDir = testSettings.workingDir();
        projectDir = testSettings.projectDir();
        styles = new Styles().initDefaults();
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

        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(3);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          1.  H1a \n" +
                "            \n" +
                "          1.1.  H2a \n");
        assertThat(pages.get(1).renderedText()).isEqualTo("" +
                "          2.  H1b \n" +
                "            \n" +
                "          2.1.  H2b \n" +
                "              \n" +
                "            2.1.1.  H3b \n" +
                "                \n" +
                "              2.1.1.1.  H4b \n" +
                "                  \n" +
                "                2.1.1.1.1.  H5b \n" +
                "                    \n" +
                "                  2.1.1.1.1.1.  H6b \n" +
                "                      \n" +
                "                    Alternatively, for H1 and H2, an underline-ish style: \n" +
                "                      \n" +
                "                                                                                                            ii \n");

        assertThat(pages.get(2).renderedText()).isEqualTo("" +
                "          3.  Alt-H1 \n" +
                "            \n" +
                "          3.1.  Alt-H2 \n" +
                "                                                                                                           iii \n");

    }

    @Test
    public void unicode_chars() throws Exception {
        process("unicode_chars_01", "/gutenberg/pegdown/unicode-chars-01.md");
    }

    @Test
    public void headers_01() throws Exception {
        process("headers_01", "/gutenberg/pegdown/headers-01.md");

        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          1.  H1a \n" +
                "            \n" +
                "          1.1.  H2a \n");
    }

    @Test
    public void headers_02() throws Exception {
        process("headers_02", "/gutenberg/pegdown/headers-02.md");

        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(2);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          1.  H1-1 \n" +
                "            \n" +
                "          Wahoo! \n" +
                "            \n");
        assertThat(pages.get(1).renderedText()).isEqualTo("" +
                "          2.  H1-2 \n" +
                "            \n" +
                "          Doooo! \n" +
                "            \n" +
                "                                                                                                            ii \n");
    }

    @Test
    public void list_01() throws Exception {
        process("list_01", "/gutenberg/pegdown/list-01.md");

        //pdfToImage(fileOut);

        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          1. First ordered list item \n" +
                "          2. Another item \n" +
                "                \n" +
                "              •  Unordered sub-list. \n" +
                "                \n" +
                "          3. Actual numbers don ' t matter, just that it ' s a number \n" +
                "                \n" +
                "              1. Ordered sub-list \n" +
                "              2. Ordered sub-list \n" +
                "                \n" +
                "          4. And another item. \n" +
                "                \n" +
                "              You can have properly indented paragraphs within list items. Notice the blank line above, and the leading \n" +
                "              spaces (at least one, but we ' ll use three here to also align the raw Markdown). \n" +
                "                \n" +
                "                \n" +
                "              To have a line break without a paragraph, you will need to use two trailing spaces.       \n" +
                "              Note that this line is separate, but within the same paragraph.       \n" +
                "              (This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.) \n" +
                "                \n" +
                "          5. And yet another \n" +
                "                \n" +
                "              If you’d like to add a paragraph in the middle of a list, and have the list numbering continue afterwards, you \n" +
                "              can indent the paragraph by four spaces. \n" +
                "                \n" +
                "            \n" +
                "            \n" +
                "          •  Unordered list can use asterisks \n" +
                "          •  Or minuses \n" +
                "          •  Or pluses \n" +
                "            \n");

    }

    @Test
    public void table_01() throws Exception {
        process("table_01", "/gutenberg/pegdown/table-01.md");

        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          Colons can be used to align columns. \n" +
                "            \n" +
                "                    Tables                             Are                                Cool \n" +
                "                    col 3 is                            right-aligned                             $1600 \n" +
                "                    col 2 is                              centered                                $12 \n" +
                "                    zebra stripes                         are neat                                  $1 \n" +
                "            \n" +
                "          The outer pipes (|) are optional, and you don ' t need to make the raw Markdown line up prettily. You can also \n" +
                "          use inline Markdown. \n" +
                "            \n" +
                "                    Markdown              Less                     Pretty \n" +
                "                    Still                        renders                    nicely \n" +
                "                    1                          2                          3 \n");
    }

    @Test
    public void table_02__with_attributes() throws Exception {
        process("table_02", "/gutenberg/pegdown/table-02.md");

        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          Colons can be used to align columns. \n" +
                "            \n" +
                "          Tables                                       Are                                          Cool \n" +
                "          col 3 is                                      right-aligned                                       $1600 \n" +
                "          col 2 is                                       centered                                          $12 \n" +
                "          zebra stripes                                  are neat                                            $1 \n");
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
                invocationContext
                        .variableResolver()
                        .declare("imageDir", new File(projectDir, "doc").toURI().toString())
                        .declare("resourcePathAsDir", new File(projectDir, "/src/test/resources").toURI().toString())
                        .declare("resourcePath", "classpath:");
                return invocationContext;
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
    public void code_03b_ditaa() throws Exception {
        processString("code_03b_ditaa", "\n" +
                "```ditaa\n" +
                "                                                             +-------------+\n" +
                "                                                     /------ | Market Book |\n" +
                "/--------------\\     submit order  /-------------\\   |       +-------------+\n" +
                "| Electronic   | ----------------> | Transaction |---/              .\n" +
                "| Trading      |                   | Router      |------ ...        .\n" +
                "| Network cBLK | <---------------- |             |---\\              .\n" +
                "\\--------------/   order status    \\-------------/   |       +-------------+\n" +
                "                                                     \\-------| Market Book |\n" +
                "                                                             +-------------+\n" +
                "```\n", Functions.<InvocationContext>identity());
    }

    @Test
    public void code_04_uml() throws Exception {
        process("code_04_uml", "/gutenberg/pegdown/code-04-uml.md");
    }

    @Test
    public void code_05_latex() throws Exception {
        process("code_05_latex", "/gutenberg/pegdown/code-05-latex-math.md");
    }

    @Test
    public void references_02() throws Exception {
        process("references_02", "/gutenberg/pegdown/references-02.md");
    }

    @Test
    public void paragraph_02() throws Exception {
        process("paragraph_02", "/gutenberg/pegdown/paragraph-02.md");
        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "                                                    Hello every body \n" +
                "                                                             \n" +
                "            \n" +
                "          How are you? \n" +
                "            \n" +
                "            \n" +
                "                                                                                                      I ' m fine \n" +
                "                                                                                                              \n");
    }

    @Test
    public void paragraph_03__empty_space_at_the_end_of_line_should_force_new_line_in_paragraph() throws Exception {
        String mkd = "" +
                "Two empty space at the end of the line  \n" +
                "should act as a new line in the paragraph\n" +
                "\n" +
                "This should be an other paragraph...\n" +
                "but with no space at the end of the line\n" +
                "\n" +
                "\n" +
                "And yet another one";
        processString("paragraph_02", mkd, Functions.<InvocationContext>identity());
        List<TextStripper.Page> pages = new TextStripper()
                .extractText(new FileInputStream(fileOut));

        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          Two empty space at the end of the line   \n" +
                "          should act as a new line in the paragraph \n" +
                "            \n" +
                "            \n" +
                "          This should be an other paragraph …   but with no space at the end of the line \n" +
                "            \n" +
                "            \n" +
                "          And yet another one \n" +
                "            \n");
    }

    @Test
    public void genericBox_01() throws Exception {
        process("genericBox_01", "/gutenberg/pegdown/genericBox-01-with-attributesText.md");

    }

    private void process(String usecase, String resourcePath) throws Exception {
        process(usecase, resourcePath, Functions.<InvocationContext>identity());
    }

    private void process(String usecase, String resourcePath, Function<InvocationContext, InvocationContext> customizer) throws Exception {
        String mkd = loadResource(resourcePath).trim();
        processString(usecase, mkd, customizer);
    }

    private void processString(String usecase, String mkd, Function<InvocationContext, InvocationContext> customizer) throws Exception {
        Reference<Parser> parserRef = new Reference<Parser>();
        ITextContext iTextContext = openDocument(usecase);

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .withPlugin(GenericBoxPlugin.class, parserRef)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        parserRef.set(processor.parser);

        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        InvocationContext context = customizer.apply(new InvocationContext(iTextContext));
        if (context == null) {
            fail("No context");
            return;
        }

        context.process(0, rootNode);
        context.flushPendingChapter();
        iTextContext.close();
    }

    private ITextContext openDocument(String method) throws FileNotFoundException, DocumentException {
        fileOut = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");
        return new ITextContextBuilder()
                .usingPygmentsStyleSheet(new FriendlyStyle())
                .usingStyles(styles)
                .build()
                .open(fileOut);
    }

    protected String loadResource(String resourceName) {
        InputStream stream = getClass().getResourceAsStream(resourceName);
        if (stream == null)
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        try {
            return IOUtils.toString(stream, "utf8");
        } catch (IOException e) {
            throw new RuntimeException("Failed to open resource " + resourceName, e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
