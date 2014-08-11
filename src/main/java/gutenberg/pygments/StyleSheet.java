package gutenberg.pygments;

import com.google.common.collect.Maps;
import gutenberg.util.RGB;
import gutenberg.util.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StyleSheet {

    private Logger log = LoggerFactory.getLogger(StyleSheet.class);

    private RGB backgroundColor;
    private RGB highlightColor;
    private Map<Token, Style> styles = Maps.newConcurrentMap();

    public StyleSheet() {
    }

    public RGB backgroundColor() {
        return backgroundColor;
    }

    protected void backgroundColor(RGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public RGB highlightColor() {
        return highlightColor;
    }

    protected void highlightColor(RGB highlightColor) {
        this.highlightColor = highlightColor;
    }

    protected void define(Token token, Style style) {
        styles.put(token, style);
    }

    protected void define(Token token) {
        define(token, Style.style());
    }

    protected static Style fg(String color) {
        return new Style().fg(color);
    }

    protected static Style bg(String color) {
        return new Style().bg(color);
    }

    protected static Style fg(RGB color) {
        return new Style().fg(color);
    }

    protected static Style bg(RGB color) {
        return new Style().bg(color);
    }

    protected Style rootStyleOf(Token token) {
        switch (token) {
            case Error:
            case Other:
            case Keyword:
            case Name:
            case Literal:
            case String:
            case Number:
            case Operator:
            case Punctuation:
            case Comment:
            case Generic:
                return getStyle(Token.Text);
            default:
                Token parent = token.parent();
                if (parent != null)
                    return rootStyleOf(parent);
        }
        return Style.style();
    }

    public Style styleOf(Token token) {
        Style combined = rootStyleOf(token);
        for (Token tok : token.path()) {
            Style style = getStyle(tok);
            combined = combined.overrides(style);
        }
        return combined;
    }

    private Style getStyle(Token tok) {
        Style style = styles.get(tok);
        if (style != null) {
            return style;
        } else {
            log.warn("No style defined for token of type: '{}'", tok);
            return Style.style();
        }
    }

    public RGB foregroundOf(Token token) {
        return styleOf(token).fg();
    }
}
