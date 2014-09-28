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

    public static final Object PARAGRAPH_SPACING_BEFORE = "paragraph-spacing-before";
    public static final Object PARAGRAPH_SPACING_AFTER = "paragraph-spacing-after";

    @Override
    public void process(int level, Node node, InvocationContext context) {
        List<Element> subs = context.collectChildren(level, node);
        Paragraph p = new Paragraph();
        for (Element sub : subs) {
            p.add(discardNewline(sub));
        }

        Float spacingBefore = context.iTextContext().<Float>getNullable(PARAGRAPH_SPACING_BEFORE).or(5f);
        Float spacingAfter = context.iTextContext().<Float>getNullable(PARAGRAPH_SPACING_AFTER).or(5f);
        p.setSpacingBefore(spacingBefore);
        p.setSpacingAfter(spacingAfter);
        context.append(p);
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
