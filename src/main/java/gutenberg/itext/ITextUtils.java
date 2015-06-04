package gutenberg.itext;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.util.Align;
import gutenberg.util.AlignFormatException;
import gutenberg.util.Attributes;
import gutenberg.util.Dimension;
import gutenberg.util.DimensionFormatException;
import gutenberg.util.RGB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ITextUtils {

    private static final Logger log = LoggerFactory.getLogger(ITextUtils.class);


    public static BaseColor toColor(RGB fg) {
        if (fg != null) {
            return new BaseColor(fg.r(), fg.g(), fg.b());
        }
        return null;
    }

    public static BaseFont inconsolata() throws IOException, DocumentException {
        return createEmbeddedFont("font/Inconsolata.otf", BaseFont.WINANSI);
    }

    public static BaseFont dejavuSansMono() throws IOException, DocumentException {
        return createEmbeddedFont("font/DejaVuSansMono.ttf", BaseFont.IDENTITY_H);
    }

    public static BaseFont createEmbeddedFont(String fontName, String encoding) throws IOException, DocumentException {
        return BaseFont.createFont(fontName, encoding, BaseFont.EMBEDDED);
    }

    public static void adjustOrScaleToFit(Image img, Dimension dim, Rectangle box) {
        if (dim == null) {
            scaleToFit(img, box);
            return;
        }

        float width = img.getWidth();
        switch (dim.unit()) {
            case Percent:
                width = box.getWidth() * dim.amount() / 100f;
                break;
            case Px:
                width = dim.amount();
                break;
        }

        // W --> w
        // H --> h  •••> h = w * H / W
        float height = width * img.getHeight() / img.getWidth();
        img.scaleAbsolute(width, height);
    }

    public static void scaleToFit(Image img, Rectangle box) {
        float scaleWidth = box.getWidth() / img.getWidth();
        float scaleHeight = box.getHeight() / img.getHeight();
        float scale = Math.min(scaleHeight, scaleWidth);
        if (scale < 1)
            img.scalePercent(scale * 100f);
    }

    public static void applyAlign(Paragraph p, Attributes attrs) {
        applyAlign(p, readAlign(attrs));
    }

    public static void applyAlign(Paragraph p, Align align) {
        if (align == null) {
            return;
        }

        switch (align) {
            case Center:
                p.setAlignment(Element.ALIGN_CENTER);
                break;
            case Right:
                p.setAlignment(Element.ALIGN_RIGHT);
                break;
            case Left:
            default:
                p.setAlignment(Element.ALIGN_LEFT);
                break;
        }
    }

    public static void applyWidth(PdfPTable table, Attributes attrs) {
        Dimension dim = readWidth(attrs);
        applyWidth(table, dim);
    }

    public static void applyWidth(PdfPTable table, Dimension dim) {
        if (dim == null) {
            return;
        }

        switch (dim.unit()) {
            case Percent:
                table.setWidthPercentage(dim.amount());
                break;
            case Px:
                table.setTotalWidth(dim.amount());
                break;
        }
    }

    public static void applyWidth(Paragraph table, Attributes attrs) {
        Dimension dim = readWidth(attrs);
        if (dim != null)
            log.warn("Paragraph width is not supported ({})", dim);
    }


    public static Dimension readWidth(Attributes attributes) {
        try {
            return attributes.getDimension("width");
        } catch (DimensionFormatException e) {
            log.warn("Unreadable width {}", attributes.getString("width"));
            return null;
        }
    }

    public static Align readAlign(Attributes attributes) {
        try {
            return attributes.getAlign("align");
        } catch (AlignFormatException e) {
            log.warn("Unreadable align {}", attributes.getString("align"));
            return null;
        }
    }

    public static void applyAttributes(Element element, Attributes attributes) {
        if (element instanceof Paragraph) {
            ITextUtils.applyAlign((Paragraph) element, attributes);
            ITextUtils.applyWidth((Paragraph) element, attributes);
        } else if (element instanceof PdfPTable) {
            ITextUtils.applyWidth((PdfPTable) element, attributes);
        }
    }

    public static Font adjustWithStyles(Font fontToAdjust, Font ref) {
        return new FontCopier(fontToAdjust)
                .size(ref.getSize())
                .style(ref.getStyle())
                .color(ref.getColor())
                .get();
    }

}
