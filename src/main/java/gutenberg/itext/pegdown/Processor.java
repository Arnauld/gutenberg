package gutenberg.itext.pegdown;

import org.pegdown.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public abstract class Processor {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public abstract void process(int level, Node node, InvocationContext context);

}
