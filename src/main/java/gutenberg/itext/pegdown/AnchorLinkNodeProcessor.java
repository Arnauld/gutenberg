package gutenberg.itext.pegdown;

import com.itextpdf.text.Chunk;
import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AnchorLinkNodeProcessor extends Processor {

    @Override
    public void process(int level, Node node, InvocationContext context) {
        TextNode tNode = (TextNode) node;
        context.append(new Chunk(tNode.getText(), context.peekFont()));
    }
}
