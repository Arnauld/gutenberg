package gutenberg.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ITextContext {
    //
    private Document document;
    private File fileOut;
    private OutputStream outStream;
    private PdfWriter pdfWriter;

    public ITextContext() {
    }

    public Document getDocument() {
        return document;
    }

    public PdfWriter getPdfWriter() {
        return pdfWriter;
    }

    public ITextContext open(File fileOut) throws FileNotFoundException, DocumentException {
        this.document = new Document();
        this.fileOut = fileOut;
        this.outStream = new FileOutputStream(fileOut);
        this.pdfWriter = PdfWriter.getInstance(document, outStream);
        //
        document.open();
        return this;
    }

    public void close() {
        document.close();
    }
}
