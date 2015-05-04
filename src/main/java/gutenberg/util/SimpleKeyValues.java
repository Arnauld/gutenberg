package gutenberg.util;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SimpleKeyValues implements KeyValues {

    private final List<KeyValues> delegates = new ArrayList<KeyValues>();
    private final Map<Object, Object> context = Maps.newHashMap();

    @Override
    public void delegateTo(KeyValues delegate) {
        delegates.add(delegate);
    }

    @Override
    public void declare(Object key, Object value) {
        context.put(key, value);
    }

    @Override
    public Map<Object, Object> asMap() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.putAll(context);

        // add in reverse order, since last delegate can override previous one
        for (int i = delegates.size() - 1; i >= 0; i--) {
            map.putAll(delegates.get(i).asMap());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getNullable(Object key) {
        for (KeyValues delegate : delegates) {
            Optional<T> opt = delegate.getNullable(key);
            if (opt.isPresent())
                return opt;
        }

        if (context.containsKey(key))
            return Optional.of((T) context.get(key));
        return Optional.absent();
    }

    @Override
    public Optional<String> getString(Object key) {
        return getNullable(key);
    }

    @Override
    public String getString(Object key, String defaultValue) {
        return getString(key).or(defaultValue);
    }

    @Override
    public Optional<Boolean> getBoolean(Object key) {
        return getNullable(key);
    }

    @Override
    public boolean getBoolean(Object key, boolean defaultValue) {
        return getBoolean(key).or(defaultValue);
    }

    @Override
    public Optional<Integer> getInteger(Object key) {
        return getNullable(key);
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        return getInteger(key).or(defaultValue);
    }
}
