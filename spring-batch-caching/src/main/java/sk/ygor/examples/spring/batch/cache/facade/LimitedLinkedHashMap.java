package sk.ygor.examples.spring.batch.cache.facade;

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private final int maximumCacheSize;

    public LimitedLinkedHashMap(int maximumCacheSize) {
        super(maximumCacheSize + 1, .75F, true);
        this.maximumCacheSize = maximumCacheSize;
    }

    public boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maximumCacheSize;
    }
}
