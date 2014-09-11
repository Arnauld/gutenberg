package gutenberg.itext.emitter;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.ditaa.GraphicsRenderer;
import gutenberg.itext.ITextContext;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.itext.model.SourceCode;
import gutenberg.util.Strings;
import gutenberg.util.WrappedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.text.TextGrid;

import java.awt.Graphics2D;
import java.io.UnsupportedEncodingException;

import static gutenberg.itext.ITextUtils.scaleToFit;
import static java.util.Arrays.asList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SourceCodeDitaaExtension implements SourceCodeEmitterExtension {
    private final Logger log = LoggerFactory.getLogger(SourceCodeDitaaExtension.class);
    private final PygmentsAdapter pygments;

    public SourceCodeDitaaExtension(PygmentsAdapter pygments) {
        this.pygments = pygments;
    }

    @Override
    public boolean accepts(String lang) {
        return asList("ditaa").contains(lang.toLowerCase());
    }

    @Override
    public void emit(SourceCode sourceCode, ITextContext context) {
        String lang = sourceCode.lang();
        String code = sourceCode.content();

        try {
            String trimmed = Strings.unindentBlock(code);

            log.debug("Initializing text grid");
            TextGrid grid = new TextGrid();
            grid.initialiseWithText(trimmed, null);

            ConversionOptions options = new ConversionOptions();
            options.renderingOptions.setRenderDebugLines(false);

            log.debug("Diagram creation");
            Diagram diagram = new Diagram(grid, options);

            PdfWriter pdfWriter = context.getPdfWriter();
            PdfContentByte cb = pdfWriter.getDirectContent();
            float width = (float) diagram.getWidth();
            float height = (float) diagram.getHeight();

            PdfTemplate template = cb.createTemplate(width, height);
            final Graphics2D g2 = new PdfGraphics2D(template, width, height);

            log.debug("Rendering diagram");
            GraphicsRenderer renderer = new GraphicsRenderer();
            renderer.render(diagram, g2, options.renderingOptions);
            g2.dispose();

            log.debug("Rendering diagram done");
            ImgTemplate imgTemplate = new ImgTemplate(template);
            scaleToFit(imgTemplate, context.getDocumentArtBox());
            context.append(imgTemplate);

        } catch (UnsupportedEncodingException e) {
            throw new WrappedRuntimeException(e);
        } catch (BadElementException e) {
            throw new WrappedRuntimeException(e);
        }
    }
}
