package gutenberg.pegdown.plugin;

import org.parboiled.common.ImmutableList;
import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LinkContentNode extends AbstractNode {

    private final String key;
    private final String text;

    public LinkContentNode(String key, String text) {
        this.key = key;
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
        return "FootnoteNode{" + key + ":" + text + '}';
    }

}
