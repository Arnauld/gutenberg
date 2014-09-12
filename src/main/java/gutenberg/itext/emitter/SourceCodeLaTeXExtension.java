package gutenberg.itext.emitter;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.itext.model.SourceCode;
import gutenberg.itext.pegdown.JLaTeXmathFontMapper;
import gutenberg.util.Strings;
import gutenberg.util.WrappedRuntimeException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;

import static gutenberg.itext.ITextUtils.scaleToFit;
import static java.util.Arrays.asList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SourceCodeLaTeXExtension implements SourceCodeEmitterExtension {
    private final Logger log = LoggerFactory.getLogger(SourceCodeLaTeXExtension.class);
    private final PygmentsAdapter pygments;
    private final Color foreground;

    public SourceCodeLaTeXExtension(PygmentsAdapter pygments) {
        this(pygments, Color.DARK_GRAY);
    }

    public SourceCodeLaTeXExtension(PygmentsAdapter pygments, Color foreground) {
        this.pygments = pygments;
        this.foreground = foreground;
    }

    @Override
    public boolean accepts(String lang) {
        return asList("formula", "math", "latex-math").contains(lang.toLowerCase());
    }

    @Override
    public void emit(SourceCode sourceCode, ITextContext context) {
        String lang = sourceCode.lang();
        String code = sourceCode.content();

        try {
            String trimmed = Strings.unindentBlock(code);

            log.debug("Initializing text grid");
            TeXFormula formula = new TeXFormula(trimmed);
            TeXIcon teXIcon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 14f);
            teXIcon.setInsets(new Insets(1, 1, 1, 1));
            teXIcon.setForeground(foreground);

            PdfWriter pdfWriter = context.getPdfWriter();
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
            scaleToFit(imgTemplate, context.getDocumentArtBox());

            context.append(imgTemplate);

        } catch (Exception e) {
            throw new WrappedRuntimeException(e);
        }

    }
}
