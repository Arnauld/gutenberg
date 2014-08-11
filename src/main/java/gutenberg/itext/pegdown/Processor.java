package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import org.pegdown.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public abstract class Processor {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public abstract List<Element> process(int level, Node node, InvocationContext context);

    protected static List<Element> elements(Element... elements) {
        return Arrays.asList(elements);
    }
}
