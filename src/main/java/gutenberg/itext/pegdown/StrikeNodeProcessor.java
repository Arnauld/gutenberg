package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StrikeNodeProcessor extends Processor {
    @Override
    public void process(int level, Node node, InvocationContext context) {
        Font font = context.peekFont();
        int style = Font.STRIKETHRU;
        context.pushFont(new Font(font.getBaseFont(), font.getSize(), font.getStyle() | style));
        context.processChildren(level, node);
        context.popFont();
    }
}
