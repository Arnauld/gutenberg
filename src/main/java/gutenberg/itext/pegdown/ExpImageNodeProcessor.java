package gutenberg.itext.pegdown;

import com.google.common.base.Optional;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.ITextContext;
import gutenberg.itext.ITextUtils;
import gutenberg.pegdown.TreeNavigation;
import gutenberg.pegdown.plugin.AttributesNode;
import gutenberg.util.Attributes;
import gutenberg.util.Dimension;
import gutenberg.util.DimensionFormatException;
import gutenberg.util.VariableResolver;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.ParaNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static gutenberg.pegdown.TreeNavigation.firstAncestorOfType;
import static gutenberg.pegdown.TreeNavigation.ofType;
import static gutenberg.pegdown.TreeNavigation.siblingBefore;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExpImageNodeProcessor extends Processor {

    private final Logger log = LoggerFactory.getLogger(ExpImageNodeProcessor.class);

    private final VariableResolver variableResolver;
    private final ITextContext iTextContext;

    public ExpImageNodeProcessor(VariableResolver variableResolver, ITextContext iTextContext) {
        this.variableResolver = variableResolver;
        this.iTextContext = iTextContext;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        ExpImageNode imageNode = (ExpImageNode) node;
        String title = imageNode.title;
        String url = variableResolver.resolve(imageNode.url);

        TreeNavigation nav = context.treeNavigation();
        Optional<TreeNavigation> attrNode =
                firstAncestorOfType(ParaNode.class)
                        .then(siblingBefore())
                        .then(ofType(AttributesNode.class))
                        .query(nav);

        Attributes attributes;
        if(attrNode.isPresent()) {
            attributes = attrNode.get().peek(AttributesNode.class).asAttributes();
        }
        else {
            attributes = new Attributes();
        }

        Dimension dim = readWidth(attributes);

        try {
            URL u;
            if (url.startsWith("classpath:/")) {
                String path = url.substring("classpath:/".length());
                u = ClassLoader.getSystemResource(path);
            } else {
                u = new URL(url);
            }

            log.info("Loading image from URL '{}'", u);
            Image image = Image.getInstance(u);
            image.setAlignment(Image.MIDDLE);
            ITextUtils.adjustOrScaleToFit(image, dim, iTextContext.getDocumentArtBox());

            Paragraph p = new Paragraph();
            p.setAlignment(Element.ALIGN_CENTER);
            p.add(new Chunk(image, 0, 0, true));
            if (title != null) {
                p.add(Chunk.NEWLINE);
                p.add(new Chunk(title));
            }

            return elements(p);
        } catch (MalformedURLException e) {
            log.error("Failed to open image url '{}'", url, e);
        } catch (IOException e) {
            log.error("Failed to open image url '{}'", url, e);
        } catch (BadElementException e) {
            log.error("Failed to open image url '{}'", url, e);
        }
        return elements();
    }

    private Dimension readWidth(Attributes attributes) {
        try {
            return attributes.getDimension("width");
        } catch (DimensionFormatException e) {
            log.warn("Unreadable width {}", attributes.getString("width"));
            return null;
        }
    }
}
