package gutenberg.itext.model;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Markdown {
    public static final Charset UTF8 = Charset.forName("utf8");

    public static Markdown fromUTF8Resource(String resourcePath) throws IOException {
        InputStream in = Markdown.class.getResourceAsStream(resourcePath);
        try {
            return fromUTF8(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static Markdown fromUTF8(InputStream in) throws IOException {
        return from(in, UTF8);
    }

    public static Markdown from(InputStream in, Charset cs) throws IOException {
        return from(new InputStreamReader(in, cs));
    }

    public static Markdown from(Reader reader) throws IOException {
        String s = IOUtils.toString(reader);
        return from(s);
    }

    public static Markdown from(String s) {
        return new Markdown(s);
    }


    private final String raw;
    private final boolean flushChapter;

    public Markdown(String raw) {
        this(raw, false);
    }

    public Markdown(String raw, boolean flushChapter) {
        this.raw = raw;
        this.flushChapter = flushChapter;
    }

    public String raw() {
        return raw;
    }

    public boolean flushChapter() {
        return flushChapter;
    }

    public Markdown flushChapter(boolean flushChapter) {
        return new Markdown(raw, flushChapter);
    }

}
