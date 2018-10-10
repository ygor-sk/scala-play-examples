package sk.ygor.examples.spring.batch.cache.word;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.ygor.examples.spring.batch.cache.CheckSumWriter;
import sk.ygor.examples.spring.batch.cache.facade.CacheFacade;

import java.lang.ref.SoftReference;
import java.util.function.Supplier;

public class WordGeneratorCachedManuallySoft implements WordSource {

    private static final Logger logger = LoggerFactory.getLogger(CheckSumWriter.class);

    private final WordGenerator generator;
    private final Supplier<CacheFacade<Integer, Word>> cacheFacadeSupplier;

    private SoftReference<CacheFacade<Integer, Word>> cacheFacadeSoftReference;

    private int gcCount = 0;

    public WordGeneratorCachedManuallySoft(WordGenerator generator, Supplier<CacheFacade<Integer, Word>> cacheFacadeSupplier) {
        this.generator = generator;
        this.cacheFacadeSupplier = cacheFacadeSupplier;
        this.cacheFacadeSoftReference = new SoftReference<>(cacheFacadeSupplier.get());
    }

    @Override
    public Word getWordForNumber(int wordNumber) {
        Word word = getCacheFacade().get(wordNumber);
        if (word == null) {
            word = generator.getWordForNumber(wordNumber);
            getCacheFacade().put(wordNumber, word);
        }
        return word;
    }

    private CacheFacade<Integer, Word> getCacheFacade() {
        CacheFacade<Integer, Word> cacheFacade = cacheFacadeSoftReference.get();
        if (cacheFacade == null) {
            gcCount++;
            logger.warn("Cache has been garbage collected. Creating empty one.");
            cacheFacade = cacheFacadeSupplier.get();
            cacheFacadeSoftReference = new SoftReference<>(cacheFacade);
        }
        return cacheFacade;
    }

    @Override
    public String getStatistics() {
        return String.format("Garbage collected: %d times. Last cache statistics: %s", gcCount, getCacheFacade().getStatistics());
    }

    @Override
    public String getName() {
        return String.format("Cached manually - %s - soft reference GC count: %d ", getCacheFacade().getName(), gcCount);
    }
}
