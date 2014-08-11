package gutenberg.itext.pegdown;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ParaNodeProcessor extends Processor {

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        List<Element> subs = context.processChildren(level, node);
        Paragraph p = new Paragraph();
        for (Element sub : subs) {
            p.add(discardNewline(sub));
        }
        p.setSpacingBefore(5);
        p.setSpacingAfter(5);
        return elements(p);
    }

    private static Element discardNewline(Element sub) {
        if (sub instanceof Chunk) {
            Chunk c = (Chunk) sub;
            if (c.getContent().equals("\n")) {
                return new Chunk(" ");
            }
        }
        return sub;
    }
}
