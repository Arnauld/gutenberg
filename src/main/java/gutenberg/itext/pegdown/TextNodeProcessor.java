package gutenberg.itext.pegdown;

import gutenberg.itext.ITextContext;
import gutenberg.itext.model.RichText;
import org.pegdown.ast.Node;
import org.pegdown.ast.TextNode;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TextNodeProcessor extends Processor {

    @Override
    public void process(int level, Node node, InvocationContext context) {
        TextNode tNode = (TextNode) node;

        RichText text = new RichText(tNode.getText(), context.peekFont());

        ITextContext iTextContext = context.iTextContext();
        iTextContext.emit(text);
    }
}
