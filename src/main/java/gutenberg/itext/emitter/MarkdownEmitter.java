package gutenberg.itext.emitter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.model.Markdown;
import gutenberg.itext.pegdown.InvocationContext;
import gutenberg.pegdown.plugin.AttributesPlugin;
import gutenberg.pegdown.plugin.GenericBoxPlugin;
import org.apache.commons.lang3.StringUtils;
import org.parboiled.common.Reference;
import org.pegdown.Extensions;
import org.pegdown.Parser;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MarkdownEmitter implements Emitter<Markdown> {

    private Logger log = LoggerFactory.getLogger(MarkdownEmitter.class);

    @Override
    public void emit(Markdown value, ITextContext context) {

        String raw = value.raw();
        if (StringUtils.isEmpty(raw)) {
            return;
        }

        Reference<Parser> parserRef = new Reference<Parser>();
        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .withPlugin(GenericBoxPlugin.class, parserRef)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        parserRef.set(processor.parser);

        RootNode rootNode = processor.parseMarkdown(raw.toCharArray());

        try {
            InvocationContext invocationContext =
                    new InvocationContext(context);
            invocationContext.process(0, rootNode);

            if (value.flushChapter())
                invocationContext.flushPendingChapter();
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
