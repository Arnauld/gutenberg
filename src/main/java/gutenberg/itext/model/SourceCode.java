package gutenberg.itext.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SourceCode {
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
}
