package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class BulletListNodeProcessor extends Processor {
    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        List<Element> subs = context.processChildren(level, node);

        com.itextpdf.text.List orderedList = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED);
        for (Element sub : subs) {
            if (!orderedList.add(sub)) {
                // wrap it
                ListItem listItem = new ListItem();
                listItem.add(sub);
                orderedList.add(listItem);
            }
        }
        return elements(orderedList);
    }
}
