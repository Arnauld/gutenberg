package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.itext.PygmentsAdapter;
import org.pegdown.ast.Node;
import org.pegdown.ast.VerbatimNode;

import java.util.Arrays;
import java.util.List;

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
        VerbatimNode vNode = (VerbatimNode) node;
        String lang = vNode.getType();
        String content = vNode.getText();
        for (VerbatimExtension extension : extensions) {
            if(extension.accepts(lang)) {
                return extension.process(lang, content);
            }
        }
        return pygmentsAdapter.process(lang, content);
    }
}
