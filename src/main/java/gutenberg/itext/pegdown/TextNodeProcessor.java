package gutenberg.itext.pegdown;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TextNodeProcessor extends Processor {
    @Override
    public void process(int level, Node node, InvocationContext context) {
        TextNode tNode = (TextNode) node;
        context.append(new Chunk(tNode.getText(), context.peekFont()));
    }
}
