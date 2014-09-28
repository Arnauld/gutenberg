package gutenberg.itext.pegdown;

import com.google.common.base.Supplier;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import gutenberg.itext.ITextUtils;
import gutenberg.itext.Styles;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.Node;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CodeNodeProcessor extends Processor {

    private final Styles styles;

    public CodeNodeProcessor(Styles styles) {
        this.styles = styles;
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        CodeNode cNode = (CodeNode) node;

        Font font = styles.getFont(Styles.INLINE_CODE_FONT).or(inlineCodeFont(styles));
        Chunk chunk = new Chunk(cNode.getText(), font);
        chunk.setBackground(styles.getColor(Styles.INLINE_CODE_BACKGROUND).or(BaseColor.GRAY));
        chunk.setGenericTag("code");

        context.append(chunk);
    }

    private Supplier<? extends Font> inlineCodeFont(final Styles styles) {
        return new Supplier<Font>() {
            @Override
            public Font get() {
                try {
                    return new Font(ITextUtils.inconsolata(), styles.defaultFontSize());
                } catch (Exception e) {
                    log.warn("Fail to retrieve font", e);
                    return FontFactory.getFont(FontFactory.COURIER, styles.defaultFontSize());
                }
            }
        };
    }
}
