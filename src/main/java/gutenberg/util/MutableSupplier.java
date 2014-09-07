package gutenberg.util;

import com.google.common.base.Supplier;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MutableSupplier<T> implements Supplier<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
