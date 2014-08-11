package gutenberg.itext.pegdown;

import com.itextpdf.text.Element;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface VerbatimExtension {
    boolean accepts(String lang);

    List<Element> process(String lang, String code);
}
