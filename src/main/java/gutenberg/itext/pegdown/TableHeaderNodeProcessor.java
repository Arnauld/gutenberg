package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.itext.CellStyler;
import org.pegdown.ast.Node;
import org.pegdown.ast.TableHeaderNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableHeaderNodeProcessor extends Processor {

    private final CellStyler headerCellStyler;

    public TableHeaderNodeProcessor(CellStyler headerCellStyler) {
        this.headerCellStyler = headerCellStyler;
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        context.pushCellStyler(headerCellStyler);
        context.processChildren(level, node);
        context.popCellStyler();
    }
}
