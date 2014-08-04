package gutenberg.pegdown.plugin;

import org.parboiled.common.ImmutableList;
import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AttributeListNode extends AbstractNode {

    private List<AttributeNode> nodes = new ArrayList<AttributeNode>();

    public AttributeListNode() {
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public List<Node> getChildren() {
        return ImmutableList.of();
    }

    @Override
    public String toString() {
        return "AttributeListNode{" + nodes + '}';
    }

    public boolean append(AttributeNode node) {
        nodes.add(node);
        return true;
    }

    public List<AttributeNode> getAttributes() {
        return nodes;
    }
}
