package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.pegdown.plugin.AttributesNode;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AttributesNodeProcessor extends Processor {

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        AttributesNode aNode = (AttributesNode) node;
        context.pushAttributes(level, aNode.asAttributes());
        return elements();
    }
}
