package gutenberg.pygments;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gutenberg.util.RGB;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StyleSheet {

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

    public Style styleOf(Token token) {
        List<Style> chain = Lists.newArrayListWithCapacity(3);
        while(token != null) {
            Style style = styles.get(token);
            chain.add(style);
            token = token.parent();
        }

        int len = chain.size();
        Style style = chain.get(len - 1);
        for(int i=1; i< len; i++)
            style = style.combine(chain.get(len - 1 - i));
        return style;
    }
}
