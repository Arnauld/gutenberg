package gutenberg.itext.pegdown;

import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import org.pegdown.ast.Node;

import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ListItemNodeProcessor extends Processor {

    @Override
    public void process(int level, Node node, InvocationContext context) {
        List<Element> subs = context.collectChildren(level, node);

        List<Element> head = Lists.newArrayList();
        Iterator<Element> iterator = subs.iterator();
        while (iterator.hasNext()) {
            Element sub = iterator.next();
            if (sub instanceof Paragraph) {
                if (!head.isEmpty()) {
                    break;
                }
            }
            if (sub instanceof Phrase || sub instanceof Chunk) {
                head.add(sub);
                iterator.remove();
                if (sub instanceof Paragraph) {
                    break;
                }
            } else {
                break;
            }
        }

        Phrase p = new Phrase();
        p.addAll(head);

        com.itextpdf.text.List subList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
        for (Element sub : subs) {
            ListItem listItem = new ListItem();
            listItem.add(sub);
            subList.add(listItem);
        }

        ListItem item = new ListItem(p);
        item.add(subList);
        //item.setListSymbol(bulletSymbol());

        context.append(item);
    }

    private static Chunk bulletSymbol() {
        return new Chunk(String.valueOf((char) 108),
                FontFactory.getFont(FontFactory.ZAPFDINGBATS, 10, BaseColor.GRAY));
    }
}
