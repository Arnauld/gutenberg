package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.ITextContext;
import gutenberg.itext.ITextUtils;
import gutenberg.itext.model.SourceCode;
import gutenberg.util.Attributes;
import org.pegdown.ast.Node;
import org.pegdown.ast.VerbatimNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class VerbatimNodeProcessor extends Processor {
    private final Logger log = LoggerFactory.getLogger(VerbatimNodeProcessor.class);

    public VerbatimNodeProcessor() {
    }

    @Override
    public void process(int level, Node node, InvocationContext context) {
        VerbatimNode vNode = (VerbatimNode) node;

        SourceCode code = convertToSourceCode(vNode);
        Attributes attributes = context.peekAttributes(level);
        applyAttributes(code, attributes);


        ITextContext iTextContext = context.iTextContext();
        List<Element> elements = iTextContext.emitButCollectElements(code);
        for (Element element : elements) {
            ITextUtils.applyAttributes(element, attributes);
            context.append(element);
        }
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
