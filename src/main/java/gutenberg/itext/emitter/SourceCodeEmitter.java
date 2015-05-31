package gutenberg.itext.emitter;

import com.itextpdf.text.Element;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.itext.model.SourceCode;
import gutenberg.util.WrappedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SourceCodeEmitter implements Emitter<SourceCode> {
    private final Logger log = LoggerFactory.getLogger(SourceCodeEmitter.class);

    private final PygmentsAdapter pygmentsAdapter;
    private final List<SourceCodeEmitterExtension> extensions;

    public SourceCodeEmitter(PygmentsAdapter pygmentsAdapter, SourceCodeEmitterExtension... extensions) {
        this(pygmentsAdapter, Arrays.asList(extensions));
    }

    public SourceCodeEmitter(PygmentsAdapter pygmentsAdapter, List<SourceCodeEmitterExtension> extensions) {
        if(pygmentsAdapter==null)
            throw new IllegalArgumentException("No pygments adapter provided");
        this.pygmentsAdapter = pygmentsAdapter;
        this.extensions = extensions;
    }

    @Override
    public void emit(SourceCode sourceCode, ITextContext context) {
        String lang = sourceCode.lang();

        try {
            for (SourceCodeEmitterExtension extension : extensions) {
                if (extension.accepts(lang)) {
                    extension.emit(sourceCode, context);
                    return;
                }
            }
        } catch (WrappedRuntimeException wre) {
            log.warn("Failed to invoke extension, fallback on raw rendering", wre.getCause());
        }

        // still there? no extension or error, let's rely directly on pygments
        List<Element> elements = pygmentsAdapter.process(sourceCode);
        context.appendAll(elements);
    }
}
