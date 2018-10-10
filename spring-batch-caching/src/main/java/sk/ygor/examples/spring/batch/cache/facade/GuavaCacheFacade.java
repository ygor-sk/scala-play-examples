package sk.ygor.examples.spring.batch.cache.facade;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaCacheFacade<K, V> implements CacheFacade<K, V> {

    private final Cache<K, V> cache;

    public GuavaCacheFacade(long maximumSize) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .recordStats()
                .build();
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public String getName() {
        return "Guava";
    }

    @Override
    public String getStatistics() {
        return cache.stats().toString();
    }
}
