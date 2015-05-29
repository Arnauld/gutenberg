package gutenberg.itext.support;

import com.google.common.base.Optional;
import gutenberg.itext.ITextContext;
import gutenberg.itext.PygmentsAdapter;
import gutenberg.itext.Styles;
import gutenberg.itext.emitter.MarkdownEmitter;
import gutenberg.itext.emitter.RichTextEmitter;
import gutenberg.itext.emitter.SourceCodeDitaaExtension;
import gutenberg.itext.emitter.SourceCodeEmitter;
import gutenberg.itext.emitter.SourceCodeLaTeXExtension;
import gutenberg.itext.model.Markdown;
import gutenberg.itext.model.RichText;
import gutenberg.itext.model.SourceCode;
import gutenberg.pygments.Pygments;
import gutenberg.pygments.StyleSheet;
import gutenberg.pygments.styles.FriendlyStyle;
import gutenberg.util.KeyValues;
import gutenberg.util.Margin;
import gutenberg.util.SimpleKeyValues;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ITextContextBuilder {
    private Styles styles;
    private PygmentsAdapter pygmentsAdapter;
    private StyleSheet pygmentsStyleSheet = new FriendlyStyle();
    private KeyValues keyValues = new SimpleKeyValues();
    private Margin margin;

    public ITextContextBuilder usingStyles(Styles styles) {
        this.styles = styles;
        this.keyValues.declare(Styles.class, styles);
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

    public ITextContextBuilder usingDocumentMargin(Margin margin) {
        this.margin = margin;
        return this;
    }

    public ITextContext build() {
        ITextContext context = new ITextContext(keyValues, getStyles(), margin);
        registerEmitters(context);
        return context;
    }

    private Styles getStyles() {
        if (styles == null) {
            Optional<Styles> stylesOpt = keyValues.getNullable(Styles.class);
            if (!stylesOpt.isPresent()) {
                styles = new Styles().initDefaults();
                keyValues.declare(Styles.class, styles);
            }
        }
        return styles;
    }

    protected void registerEmitters(ITextContext context) {
        context.register(
                SourceCode.class,
                new SourceCodeEmitter(getPygmentsAdapter(),
                        new SourceCodeDitaaExtension(getPygmentsAdapter()),
                        new SourceCodeLaTeXExtension(getPygmentsAdapter())));
        context.register(Markdown.class, new MarkdownEmitter());
        context.register(RichText.class, new RichTextEmitter());
    }

    protected PygmentsAdapter getPygmentsAdapter() {
        if (pygmentsAdapter == null) {
            pygmentsAdapter = new PygmentsAdapter(new Pygments(), pygmentsStyleSheet, getStyles());
        }
        return pygmentsAdapter;
    }

}
