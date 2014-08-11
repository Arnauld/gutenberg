package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SuperNodeProcessor extends Processor {
    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        return context.processChildren(level, node);
    }
}
