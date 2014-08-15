package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.pegdown.References;
import gutenberg.util.VariableResolver;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RefImageNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TextNode;

import java.util.List;

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
        TextNode text = (TextNode) lookupChild(refImage.referenceKey, TextNode.class);
        if (text == null) {
            log.warn("Unknown reference image structure... {}", refImage);
            return context.processChildren(level, node);
        }

        References.Ref ref = context.references().lookup(text.getText());
        if (ref != null) {
            return context.process(level, new ExpImageNode(ref.title(), ref.url(), refImage.getChildren().get(0)));
        }
        log.warn("Reference not found for image {}", text.getText());
        return elements();
    }

    private static Node lookupChild(Node node, Class<? extends Node>... childClasses) {
        Node child = node;
        for (Class<? extends Node> childClass : childClasses) {
            child = child.getChildren().get(0);
            if (!childClass.isInstance(child))
                return null;
        }
        return child;
    }
}
