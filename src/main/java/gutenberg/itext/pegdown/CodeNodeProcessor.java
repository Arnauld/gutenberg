package gutenberg.itext.pegdown;

import com.google.common.base.Supplier;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import gutenberg.itext.ITextUtils;
import gutenberg.itext.Styles;
import gutenberg.itext.model.RichText;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.Node;

import java.util.List;

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
        List<Element> elements = context.iTextContext().emitButCollectElements(new RichText(cNode.getText(), font));
        for (Element element : elements) {
            if (element instanceof Chunk) {
                Chunk chunk = (Chunk) element;
                chunk.setBackground(styles.getColor(Styles.INLINE_CODE_BACKGROUND).or(BaseColor.GRAY));
                chunk.setGenericTag("code");
            }
            context.append(element);
        }
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
