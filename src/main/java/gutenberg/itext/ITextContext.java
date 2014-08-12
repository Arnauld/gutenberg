package gutenberg.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.util.Margin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ITextContext {
    private Rectangle pageSize = PageSize.A4;
    private Margin documentMargin = new Margin(50);
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
        this.document = createDocument();
        this.fileOut = fileOut;
        this.outStream = new FileOutputStream(fileOut);
        this.pdfWriter = PdfWriter.getInstance(document, outStream);
        this.pdfWriter.setBoxSize("art", getDocumentArtBox());
        //
        document.open();
        return this;
    }

    public void close() {
        document.close();
    }

    public Document createDocument() {
        return new Document(pageSize,
                documentMargin.marginLeft,
                documentMargin.marginRight,
                documentMargin.marginTop,
                documentMargin.marginBottom);
    }

    public Rectangle getDocumentArtBox() {
        return new Rectangle(
                documentMargin.marginLeft,
                documentMargin.marginBottom,
                pageSize.getWidth() - documentMargin.marginRight,
                pageSize.getHeight() - documentMargin.marginTop);
    }
}
