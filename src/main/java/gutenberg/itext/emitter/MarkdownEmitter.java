package gutenberg.itext.emitter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Styles;
import gutenberg.itext.pegdown.InvocationContext;
import gutenberg.pegdown.plugin.AttributesPlugin;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gutenberg.itext.model.Markdown;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MarkdownEmitter implements Emitter<Markdown> {

    private Logger log = LoggerFactory.getLogger(MarkdownEmitter.class);

    @Override
    public void emit(Markdown value, ITextContext context) {
        Styles styles = context.styles();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(value.raw().toCharArray());

        try {
            InvocationContext invocationContext =
                    new InvocationContext(context, styles)
                            .variableResolver(context.variableResolver())
                            .useSections(context.sections());
            List<Element> elements = invocationContext.process(0, rootNode);
            context.appendAll(elements);
        } catch (IOException e) {
            log.error("Fail to generate markdown", e);
            emitRaw(value, context);
        } catch (DocumentException e) {
            log.error("Fail to generate markdown", e);
            emitRaw(value, context);
        }
    }

    private void emitRaw(Markdown value, ITextContext emitterContext) {
        emitterContext.append(new Paragraph(value.raw()));
    }
}
