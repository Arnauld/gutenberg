package gutenberg.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class WrappedRuntimeException extends RuntimeException {
    public WrappedRuntimeException(Throwable cause) {
        super(cause);
    }
}
