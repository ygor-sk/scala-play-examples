package sk.ygor.examples.spring.batch.cache.facade;

import java.util.Collections;
import java.util.Map;

public class MapCacheFacade<K, V> implements CacheFacade<K, V> {

    private final Map<K, V> map;
    private final String originalMapClassName;

    private long hitCount = 0;
    private long missCount = 0;

    public MapCacheFacade(Map<K, V> map) {
        this.map = Collections.synchronizedMap(map);
        this.originalMapClassName = map.getClass().getSimpleName();
    }

    @Override
    public V get(K key) {
        V result = map.get(key);
        if (result != null) {
            hitCount++;
        }
        return result;
    }

    @Override
    public void put(K key, V value) {
        missCount++;
        map.put(key, value);
    }

    @Override
    public String getName() {
        return originalMapClassName;
    }

    @Override
    public String getStatistics() {
        return String.format("Hit count: %d, miss count: %d", hitCount, missCount);
    }
}
