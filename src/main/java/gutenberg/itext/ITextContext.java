package gutenberg.itext;

import com.google.common.collect.Maps;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.util.Collector;
import gutenberg.util.Consumer;
import gutenberg.util.Margin;
import gutenberg.util.New;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ITextContext {

    private Logger log = LoggerFactory.getLogger(ITextContext.class);

    private Rectangle pageSize = PageSize.A4;
    private Margin documentMargin = new Margin(50);
    //
    private Document document;
    private File fileOut;
    private OutputStream outStream;
    private PdfWriter pdfWriter;
    //
    private final Sections sections;
    private final Styles styles;
    //
    private final Stack<Consumer<Element>> consumers = New.newStack();
    private final Map<Object, Emitter> registered = Maps.newConcurrentMap();

    public ITextContext(Sections sections, Styles styles) {
        this.sections = sections;
        this.styles = styles;
    }

    public Document getDocument() {
        return document;
    }

    public PdfWriter getPdfWriter() {
        return pdfWriter;
    }

    public ITextContext open(File fileOut) throws FileNotFoundException, DocumentException {
        this.document = createDocument();
        this.fileOut = fileOut;
        this.outStream = new FileOutputStream(fileOut);
        this.pdfWriter = PdfWriter.getInstance(document, outStream);
        this.pdfWriter.setBoxSize("art", getDocumentArtBox());
        //
        document.open();
        return this;
    }

    public void close() {
        log.info("File generated at {}", fileOut.getAbsolutePath());
        document.close();
    }

    protected Document createDocument() {
        return new Document(pageSize,
                documentMargin.marginLeft,
                documentMargin.marginRight,
                documentMargin.marginTop,
                documentMargin.marginBottom);
    }

    public Rectangle getDocumentArtBox() {
        return new Rectangle(
                documentMargin.marginLeft,
                documentMargin.marginBottom,
                pageSize.getWidth() - documentMargin.marginRight,
                pageSize.getHeight() - documentMargin.marginTop);
    }

    public Sections sections() {
        return sections;
    }

    @SuppressWarnings("unchecked")
    public <T> Emitter<T> emitterFor(Class<T> type) {
        Emitter emitter = registered.get(type);
        if (emitter == null)
            throw new IllegalArgumentException("No emitter registered for type '" + type + "'");
        return emitter;
    }

    public void appendAll(Iterable<? extends Element> elements) {
        for (Element e : elements)
            append(e);

    }

    public void append(Element element) {
        try {
            if (!consumers.isEmpty()) {
                consumers.peek().consume(element);
                return;
            }

            if (element instanceof Chapter) {
                getDocument().add(element);
                sections.leaveSection(1);
                return;
            }

            Section section = sections.currentSection();
            if (section == null) {
                getDocument().add(element);
            } else {
                section.add(element);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void emit(Class<? super T> klazz, T value) {
        Emitter<? super T> emitter = emitterFor(klazz);
        if (emitter == null)
            throw new RuntimeException("No emitter registered for type " + klazz);
        emitter.emit(value, this);
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(T value) {
        if (value instanceof SimpleEmitter) {
            ((SimpleEmitter) value).emit(this);
            return;
        }
        if (value instanceof Emitter) {
            throw new IllegalArgumentException("An Emitter cannot be emitted... " + value);
        }

        Class klazz = value.getClass();
        for (Map.Entry<Object, Emitter> entry : registered.entrySet()) {
            if (entry.getKey() instanceof Class) {
                Class supportedType = (Class) entry.getKey();
                if (supportedType.isAssignableFrom(klazz)) {
                    Emitter emitter = entry.getValue();
                    emitter.emit(value, this);
                    return;
                }
            }
        }
        throw new RuntimeException("No emitter registered or suitable for type " + klazz);
    }

    public <T> void register(Class<T> type, Emitter<? super T> emitter) {
        registered.put(type, emitter);
    }

    public void pushElementConsumer(Consumer<Element> consumer) {
        consumers.push(consumer);
    }

    public Consumer<Element> popElementConsumer() {
        return consumers.pop();
    }

    public List<Element> emitButCollectElements(Object value) {
        Collector<Element> elementCollector = new Collector<Element>();
        pushElementConsumer(elementCollector);
        try {
            emit(value);
        } finally {
            popElementConsumer();
        }
        return elementCollector.getCollected();

    }
}
