package gutenberg.itext.pegdown;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import gutenberg.itext.Colors;
import gutenberg.itext.FontCopier;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExpLinkNodeProcessor extends Processor {
    private final Logger log = LoggerFactory.getLogger(ExpLinkNodeProcessor.class);

    @Override
    public void process(int level, Node node, InvocationContext context) {
        ExpLinkNode linkNode = (ExpLinkNode) node;
        String url = context.variableResolver().resolve(linkNode.url);

        Font anchorFont = new FontCopier(context.peekFont())
                .style(Font.UNDERLINE)
                .color(Colors.DARK_RED)
                .get();

        context.pushFont(anchorFont);
        List<Element> subs = context.collectChildren(level, node);
        context.popFont();

        Phrase p = new Phrase();
        p.addAll(subs);

        Anchor anchor = new Anchor(p);
        anchor.setReference(url);
        context.append(anchor);
    }
}
