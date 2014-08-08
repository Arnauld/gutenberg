package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.TestSettings;
import gutenberg.pygments.styles.DefaultStyle;
import gutenberg.pygments.styles.MonokaiStyle;
import gutenberg.pygments.Pygments;
import gutenberg.pygments.Style;
import gutenberg.pygments.StyleSheet;
import gutenberg.pygments.TokenWithValue;
import gutenberg.pygments.Tokens;
import gutenberg.util.RGB;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PygmentsPdfTest {

    private String workingDir;
    private Document document;

    @Before
    public void setUp() {
        workingDir = new TestSettings().workingDir();
    }

    @Test
    public void simpleGenerate() throws Exception {
        openDocument("simpleGenerate");
        BaseFont bf = createEmbeddedFont("font/Inconsolata.otf", BaseFont.WINANSI);

        Pygments pygments = new Pygments();
        Tokens tokens = pygments.tokenize("clojure", "" +
                "(defn year-end-evaluation\n" +
                "  []\n" +
                "  (if (> (rand) 0.5)\n" +
                "    \"You get a raise!\"\n" +
                "    \"Better luck next year!\"))");

        StyleSheet stylesheet = new DefaultStyle();
        Paragraph p = new Paragraph();
        for (TokenWithValue token : tokens) {
            Style style = stylesheet.styleOf(token.token);

            BaseColor color = toColor(style.fg());
            int s = Font.NORMAL;
            if (style.isBold()) {
                s |= Font.BOLD;
            }
            if (style.isItalic()) {
                s |= Font.ITALIC;
            }

            Font font = new Font(bf, 12, s, color);
            Chunk o = new Chunk(token.value, font);

            RGB bg = style.bg();
            if (bg != null)
                o.setBackground(toColor(bg));
            p.add(o);
        }

        PdfPCell cell = new PdfPCell(p);
        cell.setBackgroundColor(toColor(stylesheet.backgroundColor()));
        cell.setPaddingBottom(5.0f);

        PdfPTable table = new PdfPTable(1);
        table.addCell(cell);
        document.add(table);
        closeDocument();
    }

    private static RGB firstNotNull(RGB one, RGB two) {
        if (one != null)
            return one;
        return two;
    }

    private BaseColor toColor(RGB fg) {
        if (fg != null) {
            return new BaseColor(fg.r(), fg.g(), fg.b());
        }
        return null;
    }

    private BaseFont createEmbeddedFont(String fontName, String encoding) throws Exception {
        return BaseFont.createFont(fontName, encoding, BaseFont.EMBEDDED);
    }

    private File openDocument(String method) throws FileNotFoundException, DocumentException {
        File file = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");

        document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        return file;
    }

    private void closeDocument() {
        document.close();
    }
}
