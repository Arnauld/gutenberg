package gutenberg.itext.pegdown;

import com.google.common.base.Supplier;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.ditaa.GraphicsRenderer;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.text.TextGrid;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static gutenberg.itext.pegdown.Processor.elements;
import static java.util.Arrays.asList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DitaaVerbatimExtension implements VerbatimExtension {

    private final Logger log = LoggerFactory.getLogger(DitaaVerbatimExtension.class);
    private final PygmentsAdapter pygments;
    private final Supplier<PdfWriter> pdfWriter;

    public DitaaVerbatimExtension(PygmentsAdapter pygments, Supplier<PdfWriter> pdfWriter) {
        this.pygments = pygments;
        this.pdfWriter = pdfWriter;
    }

    public boolean accepts(String lang) {
        return asList("ditaa").contains(lang.toLowerCase());
    }

    public List<Element> process(String lang, String code) {
        try {
            String trimmed = Strings.unindentBlock(code);
            TextGrid grid = new TextGrid();
            grid.initialiseWithText(trimmed, null);

            ConversionOptions options = new ConversionOptions();
            Diagram diagram = new Diagram(grid, options);

            PdfContentByte cb = pdfWriter.get().getDirectContent();
            float width = (float) diagram.getWidth();
            float height = (float) diagram.getHeight();

            PdfTemplate template = cb.createTemplate(width, height);
            final Graphics2D g2 = new PdfGraphics2D(template, width, height);

            GraphicsRenderer renderer = new GraphicsRenderer();
            renderer.render(diagram, g2, options.renderingOptions);
            g2.dispose();

            return elements(new ImgTemplate(template));

        } catch (UnsupportedEncodingException e) {
            log.error("Oops", e);
        } catch (BadElementException e) {
            log.error("Oops", e);
        }
        // error case: fallback on raw verbatim rendering
        return pygments.process(lang, code);
    }
}
