package gutenberg.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.TestSettings;
import gutenberg.itext.model.Markdown;
import gutenberg.itext.support.ITextContextBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ComplexUsecaseTest {

    private ITextContext context;

    @Before
    public void setUp() {
        context = new ITextContextBuilder().usingStyles(new Styles()).build();

    }

    @Test
    public void case_01__markdown_open_chapter() throws IOException, DocumentException {
        File fileOut = new File(new TestSettings().workingDir(), getClass().getSimpleName() + "_case01.pdf");
        context.open(fileOut);
        context.emit(Markdown.from("# Chapter"));
        context.close();

        List<TextStripper.Page> pages = getPages(fileOut);
        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).renderedText()).isEqualTo("          1.  Chapter \n");
    }

    private List<TextStripper.Page> getPages(File fileOut) throws IOException {
        return new TextStripper()
                .extractText(new FileInputStream(fileOut));
    }

    @Test
    public void case_02__markdown_open_chapter_filled_afterwards() throws IOException, DocumentException {
        File fileOut = new File(new TestSettings().workingDir(), getClass().getSimpleName() + "_case02.pdf");
        context.open(fileOut);
        for (int i = 0; i < 3; i++) {
            final int iRef = i;

            context.emit(Markdown.from("# Chapter " + iRef));
            context.emit(new SimpleEmitter() {
                @Override
                public void emit(ITextContext context) {
                    Section section = context.sections().newSection("Section" + iRef, 2);
                    Paragraph p = new Paragraph("Salut les gars!" + iRef);
                    section.add(p);
                    context.sections().leaveSection(2);
                }
            });
        }
        context.close();

        List<TextStripper.Page> pages = getPages(fileOut);
        assertThat(pages).hasSize(3);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          1.  Chapter 0 \n" +
                "            \n" +
                "          1.1.  Section0 \n" +
                "            Salut les gars!0 \n");
        assertThat(pages.get(1).renderedText()).isEqualTo("" +
                "          2.  Chapter 1 \n" +
                "            \n" +
                "          2.1.  Section1 \n" +
                "            Salut les gars!1 \n" +
                "                                                                                                            ii \n");
        assertThat(pages.get(2).renderedText()).isEqualTo("" +
                "          3.  Chapter 2 \n" +
                "            \n" +
                "          3.1.  Section2 \n" +
                "            Salut les gars!2 \n" +
                "                                                                                                           iii \n");
    }

    @Test
    public void case_03__markdown_open_chapter_filled_afterwards_with_nested_markdown() throws IOException, DocumentException {
        File fileOut = new File(new TestSettings().workingDir(), getClass().getSimpleName() + "_case03.pdf");
        context.open(fileOut);
        for (int i = 0; i < 3; i++) {
            final int iRef = i;

            context.emit(Markdown.from("# Chapter " + iRef));
            context.emit(new SimpleEmitter() {
                @Override
                public void emit(ITextContext context) {
                    Section section = context.sections().newSection("Section" + iRef, 2);
                    context.emit(Markdown.class, Markdown.from("Salut *les gars!* `" + iRef + "`"));
                    context.sections().leaveSection(2);
                }
            });
        }
        context.close();

        List<TextStripper.Page> pages = getPages(fileOut);
        assertThat(pages).hasSize(3);
        assertThat(pages.get(0).renderedText()).isEqualTo("" +
                "          1.  Chapter 0 \n" +
                "            \n" +
                "          1.1.  Section0 \n" +
                "              \n" +
                "            Salut  les gars!   0 \n" +
                "              \n");
        assertThat(pages.get(1).renderedText()).isEqualTo("" +
                "          2.  Chapter 1 \n" +
                "            \n" +
                "          2.1.  Section1 \n" +
                "              \n" +
                "            Salut  les gars!   1 \n" +
                "              \n" +
                "                                                                                                            ii \n");
        assertThat(pages.get(2).renderedText()).isEqualTo("" +
                "          3.  Chapter 2 \n" +
                "            \n" +
                "          3.1.  Section2 \n" +
                "              \n" +
                "            Salut  les gars!   2 \n" +
                "              \n" +
                "                                                                                                           iii \n");
    }
}
