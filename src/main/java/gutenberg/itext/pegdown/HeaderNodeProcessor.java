package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.Sections;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HeaderNodeProcessor extends Processor {

    private final Sections sections;

    public HeaderNodeProcessor(Sections sections) {
        this.sections = sections;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        HeaderNode hNode = (HeaderNode) node;
        int hLevel = hNode.getLevel();

        Font font = sections.sectionTitlePrimaryFont(hLevel);
        context.pushFont(font);
        List<Element> subs = context.processChildren(level, node);
        context.popFont();

        Paragraph p = new Paragraph();
        p.setFont(font);
        p.addAll(subs);

        Element element = sections.newSection(p, hLevel);
        return elements(element);
    }
}
