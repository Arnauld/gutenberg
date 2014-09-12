package gutenberg.itext.model;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SourceCode {
    public static final String MIME_TYPE = "application/x-gutenberg.sourcecode";
    private static final Charset UTF8 = Charset.forName("utf8");

    private final String lang;
    private final String content;
    private boolean showLineNumbers;

    public SourceCode(String lang, String content) {
        this.lang = lang;
        this.content = content;
    }

    public String lang() {
        return lang;
    }

    public String content() {
        return content;
    }

    public boolean showLineNumbers() {
        return showLineNumbers;
    }

    public void showLineNumbers(boolean lineson) {
        this.showLineNumbers = lineson;
    }

    public byte[] toBytes() {
        return new Gson().toJson(this).getBytes(UTF8);
    }

    public static SourceCode fromBytes(byte[] data) {
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(data), UTF8);
        return new Gson().fromJson(reader, SourceCode.class);
    }
}
