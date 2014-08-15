package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.pegdown.References;
import gutenberg.util.VariableResolver;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RefImageNode;
import org.pegdown.ast.TextNode;

import java.util.List;

import static gutenberg.pegdown.TreeNavigation.lookupChild;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RefImageNodeProcessor extends Processor {
    private final VariableResolver variableResolver;

    public RefImageNodeProcessor(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        RefImageNode refImage = (RefImageNode) node;

        @SuppressWarnings("unchecked")
        TextNode text = (TextNode) lookupChild(refImage.referenceKey, TextNode.class);

        if (text == null) {
            log.warn("Unknown reference image structure... {}", refImage);
            return context.processChildren(level, node);
        }

        References.Ref ref = context.references().lookup(text.getText());
        if (ref != null) {
            Node altNode = refImage.getChildren().get(0);
            return context.process(level, new ExpImageNode(ref.title(), ref.url(), altNode));
        }

        log.warn("Reference not found for image {}", text.getText());
        return elements();
    }


}
