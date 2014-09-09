package gutenberg.itext;

import com.google.common.base.Function;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.util.Margin;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PercentBackgroundEvent implements PdfPCellEvent {
    private final float percent;
    private final Function<Float, BaseColor> colorProviders;
    private final Margin margin;

    public PercentBackgroundEvent(int count,
                                  int total,
                                  Function<Float, BaseColor> colorProviders) {
        this(count, total, colorProviders, new Margin(0f));
    }

    public PercentBackgroundEvent(int count,
                                  int total,
                                  Function<Float, BaseColor> colorProviders,
                                  Margin margin) {
        this.colorProviders = colorProviders;
        this.margin = margin;
        if (total == 0) {
            this.percent = 0.0f;
        } else {
            this.percent = ((float) count) / ((float) total);
        }
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
        BaseColor color = colorProviders.apply(percent);
        if (color != null) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            cb.saveState();
            cb.setColorFill(color);
            cb.rectangle(
                    rect.getLeft() + margin.marginLeft,
                    rect.getBottom() + margin.marginBottom,
                    rect.getWidth() * percent - (margin.marginLeft + margin.marginRight),
                    rect.getHeight() - (margin.marginTop + margin.marginBottom));
            cb.fill();
            cb.restoreState();
        }
    }
}
