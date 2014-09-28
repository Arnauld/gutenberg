package gutenberg.itext.pegdown;

import com.itextpdf.text.Font;
import org.pegdown.ast.Node;
import org.pegdown.ast.StrongEmphSuperNode;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StrongEmphSuperNodeProcessor extends Processor {

    @Override
    public void process(int level, Node node, InvocationContext context) {
        StrongEmphSuperNode emNode = (StrongEmphSuperNode) node;
        Font font = context.peekFont();
        int style = emNode.isStrong() ? Font.BOLD : Font.ITALIC;
        context.pushFont(new Font(font.getBaseFont(), font.getSize(), font.getStyle() | style));
        context.processChildren(level, node);
        context.popFont();
    }
}
