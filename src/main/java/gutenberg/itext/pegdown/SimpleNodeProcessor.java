package gutenberg.itext.pegdown;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.pegdown.ast.Node;
import org.pegdown.ast.SimpleNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SimpleNodeProcessor extends Processor {

    private BaseColor lineColor = BaseColor.LIGHT_GRAY;
    private float lineWidth = 2.0f;

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        List<Element> subs = context.processChildren(level, node);

        SimpleNode tNode = (SimpleNode) node;
        SimpleNode.Type type = tNode.getType();
        switch (type) {
            case Apostrophe:
                return elements(new Chunk("'"));
            case Ellipsis:
                return elements(new Chunk("\u2026"));
            case Emdash:
                return elements(new Chunk("\u2014"));
            case Endash:
                return elements(new Chunk("\u2013"));
            case HRule:
                LineSeparator lineSeparator = new LineSeparator();
                lineSeparator.setLineColor(lineColor);
                lineSeparator.setLineWidth(lineWidth);
                Paragraph p = new Paragraph();
                p.add(lineSeparator);
                p.add(Chunk.NEWLINE);
                return elements(p);
            case Linebreak:
                return elements(new Chunk("\n"));
            case Nbsp:
                return elements(new Chunk("'\u00a0"));
            default:
                log.warn("Unsupported type '{}'", type);
                return subs;
        }
    }
}
