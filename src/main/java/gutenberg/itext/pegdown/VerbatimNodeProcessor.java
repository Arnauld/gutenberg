package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.itext.PygmentsAdapter;
import org.pegdown.ast.Node;
import org.pegdown.ast.VerbatimNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VerbatimNodeProcessor extends Processor {
    private final PygmentsAdapter pygmentsAdapter;

    public VerbatimNodeProcessor(PygmentsAdapter pygmentsAdapter) {
        this.pygmentsAdapter = pygmentsAdapter;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        VerbatimNode vNode = (VerbatimNode) node;
        return pygmentsAdapter.process(vNode.getType(), vNode.getText());
    }
}
