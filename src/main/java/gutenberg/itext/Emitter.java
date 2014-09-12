package gutenberg.itext;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Emitter<T> {
    void emit(T value, ITextContext context);
}
