package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import gutenberg.util.KeyValues;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class BulletListNodeProcessor extends Processor {
    public static final Object BULLET_LIST_SPACING_BEFORE = "bullet-list-spacing-before";
    public static final Object BULLET_LIST_SPACING_AFTER = "bullet-list-spacing-after";

    @Override
    public void process(int level, Node node, InvocationContext context) {
        List<Element> subs = context.collectChildren(level, node);

        com.itextpdf.text.List orderedList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
        for (Element sub : subs) {
            if (!orderedList.add(sub)) {
                // wrap it
                ListItem listItem = new ListItem();
                listItem.add(sub);
                orderedList.add(listItem);
            }
        }

        KeyValues kvs = context.iTextContext().keyValues();

        Float spacingBefore = kvs.<Float>getNullable(BULLET_LIST_SPACING_BEFORE).or(5f);
        Float spacingAfter = kvs.<Float>getNullable(BULLET_LIST_SPACING_AFTER).or(5f);

        Paragraph p = new Paragraph();
        p.add(orderedList);
        p.setSpacingBefore(spacingBefore);
        p.setSpacingAfter(spacingAfter);

        context.append(p);
    }
}
