package gutenberg.itext.pegdown;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import gutenberg.itext.Colors;
import gutenberg.itext.FontCopier;
import gutenberg.pegdown.References;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RefLinkNode;
import org.pegdown.ast.TextNode;

import java.util.List;

import static gutenberg.pegdown.TreeNavigation.lookupChild;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RefLinkNodeProcessor extends Processor {
    public RefLinkNodeProcessor() {
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        RefLinkNode refLink = (RefLinkNode) node;


        @SuppressWarnings("unchecked")
        TextNode text = (TextNode) lookupChild(refLink.referenceKey, TextNode.class);

        if (text == null) {
            log.warn("Unknown reference link structure... {}", refLink);
            context.processChildren(level, node);
            return;
        }

        References.Ref ref = context.references().lookup(text.getText());
        if (ref != null) {
            Node altNode = refLink.getChildren().get(0);
            context.process(level, new ExpLinkNode(ref.title(), ref.url(), altNode));
            return;
        }

        log.warn("Reference not found for link {}", text.getText());

    }

}
