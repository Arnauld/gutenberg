package gutenberg.pegdown.plugin;

import gutenberg.util.Attributes;
import org.parboiled.common.ImmutableList;
import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AttributesNode extends AbstractNode {

    private final String text;

    public AttributesNode(String text) {
        this.text = text;
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
        return "AttributesNode{" + text + '}';
    }

    public Attributes asAttributes() {
        return new Attributes().appendCommaSeparatedKeyValuePairs(text);
    }
}
