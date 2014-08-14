package gutenberg.itext.pegdown;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.ditaa.GraphicsRenderer;
import gutenberg.itext.ITextContext;
import gutenberg.itext.ITextUtils;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.util.Strings;
import org.pegdown.ast.VerbatimNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.graphics.CustomShapeDefinition;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.text.TextGrid;

import java.awt.Graphics2D;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import static gutenberg.itext.ITextUtils.scaleToFit;
import static gutenberg.itext.pegdown.Processor.elements;
import static java.util.Arrays.asList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VerbatimDitaaExtension implements VerbatimExtension {

    private final Logger log = LoggerFactory.getLogger(VerbatimDitaaExtension.class);
    private final PygmentsAdapter pygments;
    private final ITextContext iTextContext;

    public VerbatimDitaaExtension(PygmentsAdapter pygments, ITextContext iTextContext) {
        this.pygments = pygments;
        this.iTextContext = iTextContext;
    }

    public boolean accepts(String lang) {
        return asList("ditaa").contains(lang.toLowerCase());
    }

    public List<Element> process(int level, VerbatimNode vNode, InvocationContext context) {
        String lang = vNode.getType();
        String code = vNode.getText();

        try {
            String trimmed = Strings.unindentBlock(code);

            log.debug("Initializing text grid");
            TextGrid grid = new TextGrid();
            grid.initialiseWithText(trimmed, null);

            ConversionOptions options = new ConversionOptions();
            options.renderingOptions.setRenderDebugLines(false);

            log.debug("Diagram creation");
            Diagram diagram = new Diagram(grid, options);

            PdfWriter pdfWriter = iTextContext.getPdfWriter();
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
            scaleToFit(imgTemplate, iTextContext.getDocumentArtBox());
            return elements(imgTemplate);

        } catch (UnsupportedEncodingException e) {
            log.error("Oops", e);
        } catch (BadElementException e) {
            log.error("Oops", e);
        }

        // error case: fallback on raw verbatim rendering
        return pygments.process(lang, code, context.peekAttributes(level));
    }

}
