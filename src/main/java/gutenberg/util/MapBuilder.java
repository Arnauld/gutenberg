package gutenberg.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MapBuilder<K, V> {

    private final Map<K, V> kvs = new HashMap<K, V>();

    public MapBuilder<K, V> with(K key, V value) {
        kvs.put(key, value);
        return this;
    }

    public Map<K, V> map() {
        return kvs;
    }

    public static <K, V> MapBuilder<K, V> mapBuilder() {
        return new MapBuilder<K, V>();
    }
}
