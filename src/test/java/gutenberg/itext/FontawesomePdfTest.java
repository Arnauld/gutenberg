package gutenberg.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import gutenberg.TestSettings;
import gutenberg.font.FontAwesome;
import org.junit.Before;
import org.junit.Test;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FontawesomePdfTest {

    public static boolean dump = false;
    private static final String TEXT = "\uf0ed \uf0c2 " + (char) 0xf042 + " \uf042" + (char) Integer.parseInt("f187", 16);

    private String workingDir;
    private Document document;

    @Before
    public void setUp() {
        workingDir = new TestSettings().workingDir();
    }

    @Test
    public void simpleGenerate() throws IOException, DocumentException {
        openDocument("simpleGenerate");
        emit("font/FontAwesome.otf", BaseFont.IDENTITY_H);
        emit("font/fontawesome-webfont.ttf", BaseFont.IDENTITY_H);
        closeDocument();
    }

    @Test
    public void awesomeFont() throws Exception {
        File fileOut = generateAwesomeFontPdf();

        String actual = extractPdfText(new FileInputStream(fileOut));
        String expected = extractPdfText(getClass().getResourceAsStream("/gutenberg/itext/FontawesomeRef.pdf"));
        if (dump) {
            for (String str : expected.split("\n"))
                System.out.println(str + " " + toHexString(str));
        }
        assertThat(actual).isEqualTo(expected);
    }

    private File generateAwesomeFontPdf() throws DocumentException, IOException {
        File fileOut = openDocument("awesomeFont");

        BaseFont bf = BaseFont.createFont("font/FontAwesome.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12);
        FontAwesome awesome = FontAwesome.getInstance();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(288 / 5.23f);
        table.setWidths(new int[]{2, 1});
        for (String key : awesome.keys().toSortedList(stringComparator())) {
            table.addCell(new PdfPCell(new Phrase(key)));
            table.addCell(new PdfPCell(new Phrase(awesome.get(key), font)));
        }
        document.add(table);
        closeDocument();
        return fileOut;
    }

    /**
     * Load pdf with an other library that generates it :)
     */
    private String extractPdfText(InputStream stream) throws IOException {
        PDDocument pdfDocument = PDDocument.load(stream);
        try {
            return new PDFTextStripper().getText(pdfDocument);
        } finally {
            pdfDocument.close();
        }
    }

    private void closeDocument() {
        document.close();
    }

    private void emit(String fontName, String encoding) throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(fontName, encoding, BaseFont.EMBEDDED);
        document.add(new Paragraph(String.format("Font file: %s with encoding %s", fontName, encoding)));
        document.add(new Paragraph(String.format("iText class: %s", bf.getClass().getName())));
        Font font = new Font(bf, 12);
        document.add(new Paragraph(TEXT, font));
        document.add(new LineSeparator(0.5f, 100, null, 0, -5));
    }

    private File openDocument(String method) throws FileNotFoundException, DocumentException {
        File file = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");

        document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        return file;
    }

    private static String toHexString(String str) {
        StringBuilder b = new StringBuilder(str.length() * 2);
        for (char c : str.toCharArray()) {
            b.append(Integer.toHexString((int) c)).append(' ');
        }
        return b.toString();
    }

    private static Comparator<? super String> stringComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
    }
}
