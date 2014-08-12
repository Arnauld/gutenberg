package gutenberg.itext.pegdown;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import gutenberg.itext.PygmentsAdapter;
import org.pegdown.ast.VerbatimNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathissideris.ascii2image.core.ConversionOptions;
import org.stathissideris.ascii2image.graphics.BitmapRenderer;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.text.TextGrid;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import static gutenberg.itext.pegdown.Processor.elements;
import static java.util.Arrays.asList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VerbatimDitaaExtension implements VerbatimExtension {

    private final Logger log = LoggerFactory.getLogger(VerbatimDitaaExtension.class);
    private final PygmentsAdapter pygments;

    public VerbatimDitaaExtension(PygmentsAdapter pygments) {
        this.pygments = pygments;
    }

    public boolean accepts(String lang) {
        return asList("ditaa").contains(lang.toLowerCase());
    }

    public List<Element> process(int level, VerbatimNode vNode, InvocationContext context) {
        String lang = vNode.getType();
        String code = vNode.getText();

        try {

            TextGrid grid = new TextGrid();
            grid.initialiseWithText(code, null);

            float scale = 8.0f;

            ConversionOptions options = new ConversionOptions();
            options.renderingOptions.setScale(scale);
            options.renderingOptions.setRenderDebugLines(true);
            Diagram diagram = new Diagram(grid, options);

            RenderedImage image = new BitmapRenderer().renderToImage(diagram, options.renderingOptions);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            Image img = Image.getInstance(os.toByteArray());
            img.scalePercent(100f / scale);

            return elements(img);

        } catch (UnsupportedEncodingException e) {
            log.error("Oops", e);
        } catch (MalformedURLException e) {
            log.error("Oops", e);
        } catch (BadElementException e) {
            log.error("Oops", e);
        } catch (IOException e) {
            log.error("Oops", e);
        }
        return pygments.process(lang, code, context.peekAttributes(level));
    }
}
