package sk.ygor.examples.spring.batch.cache.facade;

public interface CacheFacade<K, V> {

    V get(K key);

    void put(K key, V value);

    String getName();

    String getStatistics();
}
