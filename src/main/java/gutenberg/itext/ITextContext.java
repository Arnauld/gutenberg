package gutenberg.itext;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.util.Collector;
import gutenberg.util.Consumer;
import gutenberg.util.Margin;
import gutenberg.util.New;
import gutenberg.util.VariableResolver;
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
    private PdfWriter pdfWriter;
    //
    private PageNumber pageNumber;
    private TableOfContents tableOfContents;
    private final Sections sections;
    private final Styles styles;
    //
    private final Map<Object, Object> context = Maps.newHashMap();
    private final Stack<Consumer<Element>> consumers = New.newStack();
    private final Map<Object, Emitter> registeredEmitters = Maps.newConcurrentMap();

    //
    private Function<PageInfos, Phrase> header;
    private Function<PageInfos, Phrase> footer;
    private VariableResolver variableResolver = new VariableResolver();

    public ITextContext(Sections sections, Styles styles) {
        this.sections = sections;
        this.styles = styles;
    }

    public VariableResolver variableResolver() {
        return variableResolver;
    }

    public TableOfContents tableOfContents() {
        return tableOfContents;
    }

    public Styles styles() {
        return styles;
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
        this.pageNumber = new PageNumber();
        this.tableOfContents = new TableOfContents(pageNumber);
        this.header = constant(new Phrase(""));
        this.footer = constant(new Phrase(""));

        OutputStream outStream = new FileOutputStream(fileOut);
        this.pdfWriter = PdfWriter.getInstance(document, outStream);
        this.pdfWriter.setBoxSize("art", getDocumentArtBox());
        this.pdfWriter.setPageEvent(pageNumber);
        this.pdfWriter.setPageEvent(tableOfContents);
        this.pdfWriter.setPageEvent(new HeaderFooter(pageNumber, styles, header, footer));
        //
        document.open();
        return this;
    }

    private static <T, R> Function<T, R> constant(final R element) {
        return new Function<T, R>() {
            @Override
            public R apply(T pageInfos) {
                return element;
            }
        };
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

    public PageNumber pageNumber() {
        return pageNumber;
    }

    @SuppressWarnings("unchecked")
    public <T> Emitter<T> emitterFor(Class<T> type) {
        Emitter emitter = registeredEmitters.get(type);
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
        for (Map.Entry<Object, Emitter> entry : registeredEmitters.entrySet()) {
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
        registeredEmitters.put(type, emitter);
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

    /**
     * Generic placeholder to store a value.
     * @param key property's key
     * @param value property's value
     */
    public void declare(Object key, Object value) {
        context.put(key, value);
    }

    /**
     * Retrieve the value declared for the key.
     *
     * @param key property's key
     * @param <T> type the property (inferred)
     * @return property's value if found otherwise <code>null</code>
     * @see #declare(Object, Object)
     * @throws java.lang.ClassCastException if the property's value is not of the inferred type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        return (T) context.get(key);
    }
}
