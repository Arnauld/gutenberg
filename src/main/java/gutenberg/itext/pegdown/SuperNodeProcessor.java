package gutenberg.itext.pegdown;

import org.pegdown.ast.Node;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SuperNodeProcessor extends Processor {
    @Override
    public void process(int level, Node node, InvocationContext context) {
        context.processChildren(level, node);
    }
}
