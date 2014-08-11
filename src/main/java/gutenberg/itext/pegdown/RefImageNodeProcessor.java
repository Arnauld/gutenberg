package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.util.VariableResolver;
import org.pegdown.ast.Node;
import org.pegdown.ast.RefImageNode;

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
        RefImageNode refImage = (RefImageNode)node;
        context.processChildren(level, node);
        return elements();
    }
}
