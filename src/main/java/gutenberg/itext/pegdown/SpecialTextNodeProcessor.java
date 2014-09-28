package gutenberg.itext.pegdown;

import com.itextpdf.text.Chunk;
import org.pegdown.ast.Node;
import org.pegdown.ast.SpecialTextNode;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SpecialTextNodeProcessor extends Processor {
    @Override
    public void process(int level, Node node, InvocationContext context) {
        SpecialTextNode tNode = (SpecialTextNode) node;
        context.append(new Chunk(tNode.getText(), context.peekFont()));
    }
}
