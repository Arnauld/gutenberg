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

    private final BaseColor lineColor;
    private final float lineWidth;

    public SimpleNodeProcessor() {
        this(BaseColor.LIGHT_GRAY, 2.0f);
    }

    public SimpleNodeProcessor(BaseColor lineColor, float lineWidth) {
        this.lineColor = lineColor;
        this.lineWidth = lineWidth;
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        List<Element> subs = context.collectChildren(level, node);

        SimpleNode tNode = (SimpleNode) node;
        SimpleNode.Type type = tNode.getType();
        switch (type) {
            case Apostrophe:
                context.append(new Chunk("'"));
                return;
            case Ellipsis:
                context.append(new Chunk("\u2026"));
                return;
            case Emdash:
                context.append(new Chunk("\u2014"));
                return;
            case Endash:
                context.append(new Chunk("\u2013"));
                return;
            case HRule:
                LineSeparator lineSeparator = new LineSeparator();
                lineSeparator.setLineColor(lineColor);
                lineSeparator.setLineWidth(lineWidth);
                Paragraph p = new Paragraph();
                p.add(lineSeparator);
                p.add(Chunk.NEWLINE);
                context.append(p);
                return;
            case Linebreak:
                context.append(new Chunk("\n"));
                return;
            case Nbsp:
                context.append(new Chunk("'\u00a0"));
                return;
            default:
                log.warn("Unsupported type '{}'", type);
                context.appendAll(subs);
        }
    }
}
