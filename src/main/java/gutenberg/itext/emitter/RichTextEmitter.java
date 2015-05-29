package gutenberg.itext.emitter;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.model.RichText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RichTextEmitter implements Emitter<RichText> {
    private final Logger log = LoggerFactory.getLogger(RichTextEmitter.class);

    @Override
    public void emit(RichText text, ITextContext context) {
        StringBuilder b = new StringBuilder();
        Font font = text.getFont();
        BaseFont baseFont = font.getBaseFont();

        for (char c : text.getText().toCharArray()) {
            if (!baseFont.charExists(c)) {
                emitText(context, b, font);
                reset(b);
                emitSymbol(context, String.valueOf(c));
            } else {
                b.append(c);
            }
        }
        emitText(context, b, font);
    }

    protected void emitSymbol(ITextContext context, String s) {
        context.append(new Chunk(s, context.styles().getSymbolFont()));
    }

    private static void reset(StringBuilder b) {
        b.setLength(0);
    }

    protected void emitText(ITextContext context, StringBuilder b, Font font) {
        if (b.length() == 0)
            return;
        context.append(new Chunk(b.toString(), font));
    }
}
