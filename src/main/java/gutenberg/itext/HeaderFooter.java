package gutenberg.itext;

import com.google.common.base.Function;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import static com.itextpdf.text.pdf.ColumnText.showTextAligned;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HeaderFooter extends PdfPageEventHelper {

    public static Function<PageInfos, Phrase> create(final Styles styles,
                                                     final Object fontKey,
                                                     final String firstPageTemplate,
                                                     final String otherPageTemplate,
                                                     final Phrase otherPage) {
        return new Function<PageInfos, Phrase>() {
            @Override
            public Phrase apply(PageInfos pageInfos) {
                Font font = styles.getFontOrDefault(fontKey);
                if (pageInfos.getRawPageNumber() == 1) {
                    if (firstPageTemplate != null)
                        return new Phrase(firstPageTemplate, font);
                } else {
                    if (otherPage != null)
                        return otherPage;
                    else if (otherPageTemplate != null)
                        return new Phrase(otherPageTemplate, font);
                }
                return null;
            }
        };
    }

    public static final String HEADER_FONT = "header-font";
    public static final String FOOTER_FONT = "footer-font";
    public static final String HEADER_LINE_COLOR = "header-line-color";

    private final PageNumber pageNumber;
    private final Styles styles;
    private final Function<PageInfos, Phrase> header;
    private final Function<PageInfos, Phrase> footer;

    private Rectangle rect;
    private boolean drawLine = false;

    public HeaderFooter(PageNumber pageNumber,
                        Styles styles,
                        Function<PageInfos, Phrase> header,
                        Function<PageInfos, Phrase> footer) {
        this.pageNumber = pageNumber;
        this.styles = styles;
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (rect == null)
            rect = writer.getBoxSize("art");

        PdfContentByte canvas = writer.getDirectContent();
        PageInfos pageInfos = pageNumber.pageInfos();
        drawHeader(canvas, pageInfos);
        drawFooter(canvas, pageInfos);
    }

    public void drawHeader(PdfContentByte canvas, PageInfos pageInfos) {
        float top = rect.getTop() + 20;
        Phrase header = headerText(pageInfos);
        if (header != null) {
            showTextAligned(canvas, Element.ALIGN_CENTER, header, (rect.getLeft() + rect.getRight()) / 2, top, 0);
        }
    }

    public void drawFooter(PdfContentByte canvas, PageInfos pageInfos) {
        if (drawLine) {
            BaseColor lineColor = styles.getColorOrDefault(HEADER_LINE_COLOR);
            canvas.saveState();
            canvas.setColorStroke(lineColor);
            canvas.setLineWidth(1.2f);
            canvas.moveTo(rect.getLeft(), rect.getBottom() - 6);
            canvas.lineTo(rect.getRight(), rect.getBottom() - 6);
            canvas.stroke();
            canvas.restoreState();
        }

        float bottom = rect.getBottom() - 20;
        Phrase footer = footerText(pageInfos);
        if (footer != null) {
            showTextAligned(canvas, Element.ALIGN_LEFT, footer, rect.getLeft(), bottom, 0);
        }

        Font footerFont = styles.getFontOrDefault(FOOTER_FONT);
        Phrase page = new Phrase(pageInfos.getFormattedPageNumber(), footerFont);
        showTextAligned(canvas, Element.ALIGN_RIGHT, page, rect.getRight(), bottom, 0);
    }

    private Phrase headerText(PageInfos pageInfos) {
        return header.apply(pageInfos);
    }

    private Phrase footerText(PageInfos pageInfos) {
        return footer.apply(pageInfos);
    }
}
