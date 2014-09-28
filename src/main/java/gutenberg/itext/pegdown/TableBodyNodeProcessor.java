package gutenberg.itext.pegdown;

import gutenberg.itext.CellStyler;
import org.pegdown.ast.Node;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TableBodyNodeProcessor extends Processor {
    private final CellStyler cellStyler;

    public TableBodyNodeProcessor(CellStyler cellStyler) {
        this.cellStyler = cellStyler;
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        context.pushCellStyler(cellStyler);
        context.processChildren(level, node);
        context.popCellStyler();
    }
}
