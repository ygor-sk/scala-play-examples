package sk.ygor.examples.spring.batch.cache;

import sk.ygor.examples.spring.batch.cache.facade.CacheFacade;
import sk.ygor.examples.spring.batch.cache.word.WordSource;

class ChecksumItemProcessorCached extends ChecksumItemProcessor {

    private final CacheFacade<Integer, Integer> cacheFacade;

    public ChecksumItemProcessorCached(WordSource wordSource, CacheFacade<Integer, Integer> cacheFacade) {
        super(wordSource);
        this.cacheFacade = cacheFacade;
    }

    @Override
    protected int getHashCodeForWordNumber(int wordNumber) {
        Integer hashCode = cacheFacade.get(wordNumber);
        if (hashCode == null) {
            hashCode = super.getHashCodeForWordNumber(wordNumber);
            cacheFacade.put(wordNumber, hashCode);
        }
        return hashCode;
    }

    public String getStatistics() {
        return cacheFacade.getStatistics();
    }

    @Override
    public String getName() {
        return "Cached processor";
    }
}
