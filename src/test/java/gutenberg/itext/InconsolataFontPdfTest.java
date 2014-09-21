package gutenberg.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import gutenberg.TestSettings;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InconsolataFontPdfTest {

    private String workingDir;
    private Document document;

    @Before
    public void setUp() {
        workingDir = new TestSettings().workingDir();
    }

    @Test
    public void simpleGenerate() throws IOException, DocumentException {
        openDocument("simpleGenerate");
        emit("font/Inconsolata.otf", BaseFont.WINANSI);
        closeDocument();
    }

    private void closeDocument() {
        document.close();
    }

    private void emit(String fontName, String encoding) throws DocumentException, IOException {
        String text = "" +
                "(defn year-end-evaluation\n" +
                "  []\n" +
                "  (if (> (rand) 0.5)\n" +
                "    \"You get a raise!\"\n" +
                "    \"Better luck next year!\"))";


        BaseFont bf = BaseFont.createFont(fontName, encoding, BaseFont.EMBEDDED);
        document.add(new Paragraph(String.format("Font file: %s with encoding %s", fontName, encoding)));
        document.add(new Paragraph(String.format("iText class: %s", bf.getClass().getName())));
        Font font = new Font(bf, 12);
        document.add(new Paragraph(text, font));
        document.add(new LineSeparator(0.5f, 100, null, 0, -5));
    }

    private File openDocument(String method) throws FileNotFoundException, DocumentException {
        File file = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");

        document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        return file;
    }
}
