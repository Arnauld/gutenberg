package gutenberg.itext.pegdown;

import gutenberg.pegdown.References;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RefImageNode;
import org.pegdown.ast.TextNode;

import static gutenberg.pegdown.TreeNavigation.lookupChild;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RefImageNodeProcessor extends Processor {

    public RefImageNodeProcessor() {
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        RefImageNode refImage = (RefImageNode) node;

        @SuppressWarnings("unchecked")
        TextNode text = (TextNode) lookupChild(refImage.referenceKey, TextNode.class);

        if (text == null) {
            log.warn("Unknown reference image structure... {}", refImage);
            context.processChildren(level, node);
            return;
        }

        References.Ref ref = context.references().lookup(text.getText());
        if (ref != null) {
            Node altNode = refImage.getChildren().get(0);
            context.process(level, new ExpImageNode(ref.title(), ref.url(), altNode));
            return;
        }

        log.warn("Reference not found for image {}", text.getText());
    }


}
