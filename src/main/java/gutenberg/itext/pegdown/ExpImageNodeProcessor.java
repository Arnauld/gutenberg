package gutenberg.itext.pegdown;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import gutenberg.util.VariableResolver;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExpImageNodeProcessor extends Processor {

    private final Logger log = LoggerFactory.getLogger(ExpImageNodeProcessor.class);

    private final VariableResolver variableResolver;

    public ExpImageNodeProcessor(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        ExpImageNode imageNode = (ExpImageNode) node;
        String title = imageNode.title;
        String url = variableResolver.resolve(imageNode.url);

        try {
            URL u = new URL(url);
            Image image = Image.getInstance(u);
            return elements(new Chunk(image, 0, 0, true));
        } catch (MalformedURLException e) {
            log.error("Failed to open image url '{}'", url, e);
        } catch (IOException e) {
            log.error("Failed to open image url '{}'", url, e);
        } catch (BadElementException e) {
            log.error("Failed to open image url '{}'", url, e);
        }
        return elements();
    }
}
