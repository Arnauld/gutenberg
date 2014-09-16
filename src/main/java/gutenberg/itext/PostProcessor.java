package gutenberg.itext;

import java.io.File;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface PostProcessor {
    void postProcess(ITextContext context, File fileIn, File fileOut);
}
