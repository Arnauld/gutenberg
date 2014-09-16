package gutenberg.itext;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableOfContentsPostProcessor implements PostProcessor {

    private static final String TOC_ENTRY_FONT = "tableOfContents-entry-font";

    private Logger log = LoggerFactory.getLogger(TableOfContentsPostProcessor.class);

    private final HeaderFooter headerFooter;

    public TableOfContentsPostProcessor(HeaderFooter headerFooter) {
        this.headerFooter = headerFooter;
    }

    @Override
    public void postProcess(ITextContext context, File fileIn, File fileOut) {
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(fileIn);
            out = new FileOutputStream(fileOut);

            PageNumber pageNumber = context.pageNumber();

            int startPage = pageNumber.lookupFrontMatterLastPage();
            ColumnText ct = generateTableOfContent(context);
            pageNumber.continueFrontMatter();

            PdfReader reader = new PdfReader(in);
            PdfStamper stamper = new PdfStamper(reader, out);
            while (true) {
                stamper.insertPage(++startPage, reader.getPageSize(1));

                PdfContentByte under = stamper.getUnderContent(startPage);
                pageNumber.notifyPageChange();
                headerFooter.drawFooter(under, pageNumber.pageInfos());

                PdfContentByte canvas = stamper.getOverContent(startPage);
                ct.setCanvas(canvas);
                ct.setSimpleColumn(36, 36, 559, 770);
                if (!ColumnText.hasMoreText(ct.go()))
                    break;
            }
            stamper.close();

        } catch (FileNotFoundException e) {
            log.error("Unable to reopen temporary generated file ({})", fileIn, e);
        } catch (DocumentException e) {
            log.error("Error during report post-processing from: {}, to: {}", fileIn, fileOut, e);
        } catch (IOException e) {
            log.error("Error during report post-processing from: {}, to: {}", fileIn, fileOut, e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    public ColumnText generateTableOfContent(ITextContext context) {
        ColumnText ct = new ColumnText(null);

        Styles styles = context.styles();
        Chunk CONNECT = connectChunk(styles);
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingBefore(20.0f); // first paragraph only

        ct.addElement(new Paragraph("Table of content", styles.sectionTitleFontForLevel(1)));
        ct.addElement(new Paragraph(""));

        Font entryFont = styles.getFontOrDefault(TOC_ENTRY_FONT);
        TableOfContents tableOfContents = context.tableOfContents();
        for (TableOfContents.Entry entry : tableOfContents.getEntries()) {
            //if (entry.isExtra())
            //    continue;

            Chunk chunk = new Chunk(entry.getText(), entryFont);
            paragraph.add(chunk);
            paragraph.add(CONNECT);
            paragraph.add(new Chunk("" + entry.getFormattedPageNumber(), entryFont));

            float indent = 10.0f * entry.getLevel();
            paragraph.setIndentationLeft(indent);

            ct.addElement(paragraph);
            paragraph = new Paragraph();
        }
        return ct;
    }

    private Chunk connectChunk(Styles styles) {
        return new Chunk(new LineSeparator(0.5f, 95, styles.defaultColor(), Element.ALIGN_CENTER, -.5f));
    }
}
