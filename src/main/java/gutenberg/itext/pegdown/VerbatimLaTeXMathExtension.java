package gutenberg.itext.pegdown;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Element;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.util.Strings;
import org.pegdown.ast.VerbatimNode;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.List;

import static gutenberg.itext.ITextUtils.scaleToFit;
import static gutenberg.itext.pegdown.Processor.elements;
import static java.util.Arrays.asList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VerbatimLaTeXMathExtension implements VerbatimExtension {

    private final Logger log = LoggerFactory.getLogger(VerbatimLaTeXMathExtension.class);
    private final PygmentsAdapter pygments;
    private final ITextContext iTextContext;
    private final Color foreground;

    public VerbatimLaTeXMathExtension(PygmentsAdapter pygments, ITextContext iTextContext) {
        this(pygments, iTextContext, Color.DARK_GRAY);
    }

    public VerbatimLaTeXMathExtension(PygmentsAdapter pygments, ITextContext iTextContext, Color foreground) {
        this.pygments = pygments;
        this.iTextContext = iTextContext;
        this.foreground = foreground;
    }

    public boolean accepts(String lang) {
        return asList("formula", "math", "latex-math").contains(lang.toLowerCase());
    }

    public List<Element> process(int level, VerbatimNode vNode, InvocationContext context) {
        String lang = vNode.getType();
        String code = vNode.getText();

        try {
            String trimmed = Strings.unindentBlock(code);

            log.debug("Initializing text grid");
            TeXFormula formula = new TeXFormula(trimmed);
            TeXIcon teXIcon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 14f);
            teXIcon.setInsets(new Insets(1, 1, 1, 1));
            teXIcon.setForeground(foreground);

            PdfWriter pdfWriter = iTextContext.getPdfWriter();
            PdfContentByte cb = pdfWriter.getDirectContent();
            float width = (float) teXIcon.getIconWidth();
            float height = (float) teXIcon.getIconHeight();

            PdfTemplate template = cb.createTemplate(width, height);
            Graphics2D g2 = new PdfGraphics2D(template, width, height, new JLaTeXmathFontMapper());

            log.debug("Rendering formula");
            teXIcon.paintIcon(null, g2, 0, 0);
            g2.dispose();

            log.debug("Rendering diagram done");
            ImgTemplate imgTemplate = new ImgTemplate(template);
            scaleToFit(imgTemplate, iTextContext.getDocumentArtBox());
            return elements(imgTemplate);

        } catch (Exception e) {
            log.error("Oops", e);
        }

        // error case: fallback on raw verbatim rendering
        return pygments.process(lang, code, context.peekAttributes(level));
    }

}
