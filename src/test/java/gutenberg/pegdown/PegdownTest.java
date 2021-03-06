package gutenberg.pegdown;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableNode;
import org.pegdown.ast.TableRowNode;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(JUnitParamsRunner.class)
public class PegdownTest extends AbstractPegdownTest {

    @Test
    @Parameters({
            "astText.md, astText.ast",
    })
    public void dumpAST(String mkdSrc, String astSrc) throws IOException {
        String mkd = loadResource(mkdSrc).trim();
        String ast = loadResource(astSrc).trim();

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        RootNode rootNode = processor.parseMarkdown(normalize(mkd).toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(ast));
    }

    @Test
    @Ignore("Multiline cell not supported")
    public void table_multi_line_cell() throws Exception {
        String mkd = loadResource("table-multiline-cells.md").trim();
        String ast = loadResource("table-multiline-cells.ast").trim();

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        String s = dumpAST(rootNode);
        assertThat(normalize(s.trim())).isEqualTo(normalize(ast));
    }

    @Test
    public void table1AST__with_caption_column_alignment_colspan() throws Exception {
        String mkd = loadResource("table1.md").trim();
        String ast = loadResource("table1.ast").trim();

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        RootNode rootNode = processor.parseMarkdown(normalize(mkd).toCharArray());

        String s = new Dumper(normalize(mkd)).dump(rootNode).out.toString();
        assertThat(normalize(s.trim())).isEqualTo(normalize(ast));
    }

    @Test
    public void line_break_within_paragraph() throws Exception {
        String mkd = "" +
                "Two empty space at the end of the line  \n" +
                "should act as a new line in the paragraph\n" +
                "\n" +
                "This should be an other paragraph...\n" +
                "but with no space at the end of the line\n" +
                "\n" +
                "\n" +
                "And yet another one";

        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        RootNode rootNode = processor.parseMarkdown(normalize(mkd).toCharArray());

        String s = new Dumper(normalize(mkd)).dump(rootNode).out.toString();
        assertThat(normalize(s.trim())).isEqualTo(normalize("" +
                "RootNode [0-185]\n" +
                "  ParaNode [0-84]\n" +
                "    SuperNode [0-82]\n" +
                "      TextNode [0-38] 'Two empty space at the end of the line'\n" +
                "      SimpleNode [38-41] Linebreak\n" +
                "      TextNode [41-82] 'should act as a new line in the paragraph'\n" +
                "  ParaNode [84-164]\n" +
                "    SuperNode [84-161]\n" +
                "      TextNode [84-117] 'This should be an other paragraph'\n" +
                "      SimpleNode [117-120] Ellipsis\n" +
                "      SimpleNode [120-121] Linebreak\n" +
                "      TextNode [121-161] 'but with no space at the end of the line'\n" +
                "  ParaNode [164-185]\n" +
                "    SuperNode [164-183]\n" +
                "      TextNode [164-183] 'And yet another one'"));
    }

    public static class Dumper {
        final String data;
        StringBuilder out = new StringBuilder();

        public Dumper(String data) {
            this.data = data;
        }

        public Dumper dump(Node node) {
            dump(0, node);
            return this;
        }

        public void dump(int indent, Node node) {
            String prefix = indent(indent);
            out.append(prefix).append(toString(node)).append('\n');
            for (Node child : node.getChildren())
                dump(indent + 1, child);
        }

        public String toString(Node node) {
            if (node instanceof TableNode)
                return toStringNode((TableNode) node);
            if (node instanceof TableRowNode)
                return toStringNode((TableRowNode) node);
            if (node instanceof TableCellNode)
                return toStringNode((TableCellNode) node);
            return node.toString();
        }

        private String toStringNode(TableNode node) {
            return node.toString() + ", " + toStringNodes(node.getColumns());
        }

        private String toStringNode(TableRowNode node) {
            return node.toString() + ", '" + escape(data.substring(node.getStartIndex(), node.getEndIndex())) + "'";
        }

        private String escape(String s) {
            return s.replace("\n", "\\n");
        }

        private String toStringNodes(List<TableColumnNode> columns) {
            StringBuilder b = new StringBuilder();
            b.append("[");
            for (TableColumnNode n : columns)
                b.append(n.getAlignment()).append(", ");
            if (b.length() > 1)
                b.setLength(b.length() - 2);
            b.append("]");
            return b.toString();
        }

        private String toStringNode(TableCellNode node) {
            return node.toString() + ", colspan: " + node.getColSpan();
        }

        private String indent(int indent) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < indent; i++)
                b.append("  ");
            return b.toString();
        }
    }
}
