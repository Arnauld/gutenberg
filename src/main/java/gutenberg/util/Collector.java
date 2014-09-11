package gutenberg.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Collector<T> implements Consumer<T> {

    private final List<T> collected = Lists.newArrayList();

    public List<T> getCollected() {
        return collected;
    }

    @Override
    public void consume(T value) {
        collected.add(value);
    }
}
