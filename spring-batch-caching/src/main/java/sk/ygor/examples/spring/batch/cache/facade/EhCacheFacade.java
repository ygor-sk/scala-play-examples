package sk.ygor.examples.spring.batch.cache.facade;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.spi.service.StatisticsService;
import org.ehcache.core.statistics.CacheStatistics;
import org.ehcache.impl.internal.statistics.DefaultStatisticsService;

public class EhCacheFacade<K, V> implements CacheFacade<K, V> {

    private final Cache<K, V> cache;
    private final StatisticsService statisticsService;

    public EhCacheFacade(Class<K> keyClass, Class<V> valueClass, int maxSize) {
        statisticsService = new DefaultStatisticsService();

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .using(statisticsService)
                .build();
        cacheManager.init();

        this.cache = cacheManager.createCache(
                "VCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, valueClass, ResourcePoolsBuilder.heap(maxSize))
        );

    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public String getName() {
        return "EhCache";
    }

    @Override
    public String getStatistics() {
        CacheStatistics statistics = statisticsService.getCacheStatistics("VCache");
        return "Hits: " + statistics.getCacheHits() + " Misses: " + statistics.getCacheMisses();
    }

}
