package gutenberg.itext.support;

import gutenberg.itext.ITextContext;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.itext.Sections;
import gutenberg.itext.Styles;
import gutenberg.itext.emitter.SourceCodeDitaaExtension;
import gutenberg.itext.emitter.SourceCodeEmitter;
import gutenberg.itext.emitter.SourceCodeLaTeXExtension;
import gutenberg.itext.model.SourceCode;
import gutenberg.pygments.Pygments;
import gutenberg.pygments.StyleSheet;
import gutenberg.pygments.styles.FriendlyStyle;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ITextContextBuilder {
    private Styles styles;
    private PygmentsAdapter pygmentsAdapter;
    private StyleSheet pygmentsStyleSheet = new FriendlyStyle();

    public ITextContextBuilder usingStyles(Styles styles) {
        this.styles = styles;
        return this;
    }

    public ITextContextBuilder usingPygmentsStyleSheet(StyleSheet pygmentsStyleSheet) {
        this.pygmentsStyleSheet = pygmentsStyleSheet;
        return this;
    }

    public ITextContextBuilder usingPygmentsAdapter(PygmentsAdapter pygmentsAdapter) {
        this.pygmentsAdapter = pygmentsAdapter;
        return this;
    }

    public ITextContext build() {
        ITextContext context = new ITextContext(new Sections(styles), styles);
        registerEmitters(context);
        return context;
    }

    protected void registerEmitters(ITextContext context) {
        context.register(SourceCode.class,
                new SourceCodeEmitter(getPygmentsAdapter(),
                        new SourceCodeDitaaExtension(getPygmentsAdapter()),
                        new SourceCodeLaTeXExtension(getPygmentsAdapter())));
    }

    protected PygmentsAdapter getPygmentsAdapter() {
        if (pygmentsAdapter == null) {
            pygmentsAdapter = new PygmentsAdapter(new Pygments(), pygmentsStyleSheet, styles);
        }
        return pygmentsAdapter;
    }
}
