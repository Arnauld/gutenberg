package gutenberg.util;

import com.google.common.base.Optional;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface KeyValues {
    /**
     * Declare a delegate <code>KeyValues</code>. It allows to override existing values
     * with new ones. If value is not found within any declared delegated, value is searched
     * locally.
     */
    void delegateTo(KeyValues delegate);

    /**
     * Generic placeholder to store a value.
     *
     * @param key   property's key
     * @param value property's value
     */
    void declare(Object key, Object value);

    /**
     * Retrieve the value declared for the key.
     *
     * @param key property's key
     * @param <T> type the property (inferred)
     * @return property's value or {@link com.google.common.base.Optional#absent()}
     * @throws java.lang.ClassCastException if the property's value is not of the inferred type
     * @see #declare(Object, Object)
     */
    <T> Optional<T> getNullable(Object key);

    Map<Object, Object> asMap();

    Optional<String> getString(Object propertyKey);

    String getString(Object propertyKey, String defaultValue);

    Optional<Boolean> getBoolean(Object propertyKey);

    boolean getBoolean(Object propertyKey, boolean defaultValue);

    Optional<Integer> getInteger(Object propertyKey);

    int getInteger(String propertyKey, int defaultValue);

}
