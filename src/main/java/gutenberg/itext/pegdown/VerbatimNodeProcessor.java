package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.pegdown.plugin.Attributes;
import org.pegdown.ast.Node;
import org.pegdown.ast.VerbatimNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VerbatimNodeProcessor extends Processor {
    private final PygmentsAdapter pygmentsAdapter;
    private final List<VerbatimExtension> extensions;

    public VerbatimNodeProcessor(PygmentsAdapter pygmentsAdapter, VerbatimExtension... extensions) {
        this(pygmentsAdapter, Arrays.asList(extensions));
    }

    public VerbatimNodeProcessor(PygmentsAdapter pygmentsAdapter, List<VerbatimExtension> extensions) {
        this.pygmentsAdapter = pygmentsAdapter;
        this.extensions = extensions;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        Attributes attributes = context.peekAttributes(level);

        VerbatimNode vNode = (VerbatimNode) node;
        String lang = vNode.getType();
        String content = vNode.getText();

        List<Element> elems = null;
        for (VerbatimExtension extension : extensions) {
            if (extension.accepts(lang)) {
                elems = extension.process(level, vNode, context);
                break;
            }
        }
        if (elems == null)
            elems = pygmentsAdapter.process(lang, content, attributes);
        return elems;
    }

}
