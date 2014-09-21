package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DefaultProcessor extends Processor {
    public void process(int level, Node node, InvocationContext context) {
        context.processChildren(level, node);
    }
}
