package gutenberg.pegdown.plugin;

import org.pegdown.ast.Node;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.Visitor;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GenericBoxNode extends SuperNode {

    public GenericBoxNode(Node subTree) {
        this(Arrays.asList(subTree));
    }

    public GenericBoxNode(List<Node> children) {
        super(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return super.toString() + "'" + getChildren();
    }
}
