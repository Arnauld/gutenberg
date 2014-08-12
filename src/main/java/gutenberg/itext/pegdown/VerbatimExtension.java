package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;
import org.pegdown.ast.Node;
import org.pegdown.ast.VerbatimNode;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface VerbatimExtension {
    boolean accepts(String lang);

    List<Element> process(int level, VerbatimNode node, InvocationContext context);
}
