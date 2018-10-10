package sk.ygor.examples.spring.batch.cache.word;

import sk.ygor.examples.spring.batch.cache.facade.CacheFacade;

public class WordGeneratorCachedManually implements WordSource {

    private final WordGenerator generator;
    private final CacheFacade<Integer, Word> cacheFacade;

    public WordGeneratorCachedManually(WordGenerator generator, CacheFacade<Integer, Word> cacheFacade) {
        this.generator = generator;
        this.cacheFacade = cacheFacade;
    }

    @Override
    public Word getWordForNumber(int wordNumber) {
        Word word = cacheFacade.get(wordNumber);
        if (word == null) {
            word = generator.getWordForNumber(wordNumber);
            cacheFacade.put(wordNumber, word);
        }
        return word;
    }

    @Override
    public String getStatistics() {
        return cacheFacade.getStatistics();
    }

    @Override
    public String getName() {
        return "Cached manually - " + cacheFacade.getName();
    }
}
