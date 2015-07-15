package gutenberg.itext.pegdown;

import com.google.common.base.Optional;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.ITextUtils;
import gutenberg.pegdown.TreeNavigation;
import gutenberg.pegdown.plugin.AttributesNode;
import gutenberg.util.Attributes;
import gutenberg.util.KeyValues;
import org.pegdown.ast.Node;
import org.pegdown.ast.ParaNode;

import java.util.List;

import static gutenberg.pegdown.TreeNavigation.*;

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

        KeyValues kvs = context.iTextContext().keyValues();

        Float spacingBefore = kvs.<Float>getNullable(PARAGRAPH_SPACING_BEFORE).or(5f);
        Float spacingAfter = kvs.<Float>getNullable(PARAGRAPH_SPACING_AFTER).or(5f);
        p.setSpacingBefore(spacingBefore);
        p.setSpacingAfter(spacingAfter);

        applyAttributes(context, p);

        context.append(p);
    }

    private void applyAttributes(InvocationContext context, Paragraph p) {
        Attributes attributes = lookupAttributes(context);
        ITextUtils.applyAttributes(p, attributes);
    }

    private static Element discardNewline(Element sub) {
        if (sub instanceof Chunk) {
            Chunk c = (Chunk) sub;
            String content = c.getContent();
            if (content.endsWith("\n") && content.length() < 2) {
                return new Chunk(" ");
            }
        }
        return sub;
    }

    private Attributes lookupAttributes(InvocationContext context) {
        TreeNavigation nav = context.treeNavigation();
        Optional<TreeNavigation> attrNode =
                firstAncestorOfType(ParaNode.class)
                        .then(siblingBefore())
                        .then(ofType(AttributesNode.class))
                        .query(nav);

        Attributes attributes;
        if (attrNode.isPresent()) {
            attributes = attrNode.get().peek(AttributesNode.class).asAttributes();
        } else {
            attributes = new Attributes();
        }
        return attributes;
    }

}
