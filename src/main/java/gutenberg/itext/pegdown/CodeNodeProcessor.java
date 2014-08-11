package gutenberg.itext.pegdown;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.Node;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CodeNodeProcessor extends Processor {

    private final Font codeFont;
    private final BaseColor codeBackground;

    public CodeNodeProcessor(Font codeFont, BaseColor codeBackground) {
        this.codeFont = codeFont;
        this.codeBackground = codeBackground;
    }

    @Override
    public List<Element> process(int level, Node node, InvocationContext context) {
        CodeNode cNode = (CodeNode) node;
        Chunk chunk = new Chunk(cNode.getText(), codeFont);
        chunk.setBackground(codeBackground);
        chunk.setGenericTag("code");
        return elements(chunk);
    }
}
