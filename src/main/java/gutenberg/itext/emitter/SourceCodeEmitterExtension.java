package gutenberg.itext.emitter;

import gutenberg.itext.Emitter;
import gutenberg.itext.model.SourceCode;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface SourceCodeEmitterExtension extends Emitter<SourceCode> {
    boolean accepts(String lang);
}
