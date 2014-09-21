package gutenberg.itext.pegdown;

import gutenberg.pegdown.plugin.AttributesNode;
import org.pegdown.ast.Node;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AttributesNodeProcessor extends Processor {

    @Override
    public void process(int level, Node node, InvocationContext context) {
        AttributesNode aNode = (AttributesNode) node;
        context.pushAttributes(level, aNode.asAttributes());
    }
}
