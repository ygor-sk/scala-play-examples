package sk.ygor.examples.spring.batch.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ygor.examples.spring.batch.cache.facade.EhCacheFacade;
import sk.ygor.examples.spring.batch.cache.facade.GuavaCacheFacade;
import sk.ygor.examples.spring.batch.cache.facade.LimitedLinkedHashMap;
import sk.ygor.examples.spring.batch.cache.facade.MapCacheFacade;
import sk.ygor.examples.spring.batch.cache.word.*;

import java.util.HashMap;

@Service
public class WordSourceFactory {

    private final WordGenerator wordGenerator;
    private final WordGeneratorCachedBySpring wordGeneratorCachedBySpring;

    @Autowired
    public WordSourceFactory(WordGenerator wordGenerator,
                             WordGeneratorCachedBySpring wordGeneratorCachedBySpring) {
        this.wordGenerator = wordGenerator;
        this.wordGeneratorCachedBySpring = wordGeneratorCachedBySpring;
    }

    public WordSource plain() {
        return wordGenerator;
    }

    public WordSource cachedBySpring() {
        return wordGeneratorCachedBySpring;
    }

    public WordSource cachedByHashMap() {
        return new WordGeneratorCachedManually(wordGenerator, new MapCacheFacade<>(new HashMap<>()));
    }

    public WordSource cachedByLinkedHashMap(int maximumCacheSize) {
        return new WordGeneratorCachedManually(wordGenerator, new MapCacheFacade<>(new LimitedLinkedHashMap<>(maximumCacheSize)));
    }

    public WordSource cachedByGuava(int maximumCacheSize) {
        return new WordGeneratorCachedManually(wordGenerator, new GuavaCacheFacade<>(maximumCacheSize));
    }

    public WordSource cachedByEhCache(int maximumCacheSize) {
        return new WordGeneratorCachedManually(wordGenerator, new EhCacheFacade<>(Integer.class, Word.class, maximumCacheSize));
    }

    public WordSource cachedByHashMapSoftReference() {
        return new WordGeneratorCachedManuallySoft(wordGenerator, () -> new MapCacheFacade<>(new HashMap<>()));
    }


}
