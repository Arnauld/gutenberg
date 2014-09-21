package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import gutenberg.itext.ITextContext;
import gutenberg.itext.model.SourceCode;
import gutenberg.util.Attributes;
import org.pegdown.ast.Node;
import org.pegdown.ast.VerbatimNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VerbatimNodeProcessor extends Processor {

    public VerbatimNodeProcessor() {
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        VerbatimNode vNode = (VerbatimNode) node;

        SourceCode code = convertToSourceCode(vNode);
        Attributes attributes = context.peekAttributes(level);
        applyAttributes(code, attributes);


        ITextContext iTextContext = context.iTextContext();
        iTextContext.emit(code);
    }

    private SourceCode convertToSourceCode(VerbatimNode vNode) {
        String lang = vNode.getType();
        String content = vNode.getText();
        return new SourceCode(lang, content);
    }

    private void applyAttributes(SourceCode code, Attributes attributes) {
        code.showLineNumbers(attributes.isOn("linenos"));
    }

}
