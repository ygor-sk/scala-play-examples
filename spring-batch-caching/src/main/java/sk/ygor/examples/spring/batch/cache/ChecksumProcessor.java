package sk.ygor.examples.spring.batch.cache;

import org.springframework.batch.item.ItemProcessor;
import sk.ygor.examples.spring.batch.cache.row.Row;
import sk.ygor.examples.spring.batch.cache.word.WordSource;

class ChecksumProcessor implements ItemProcessor<Row, Integer> {

    private final WordSource wordSource;

    public ChecksumProcessor(WordSource wordSource) {
        this.wordSource = wordSource;
    }

    @Override
    public Integer process(Row row) {
        int checkSum = 0;
        for (int wordNumber : row.getWordNumbers()) {
            checkSum += getHashCodeForWordNumber(wordNumber);
        }
        return checkSum;
    }

    protected int getHashCodeForWordNumber(int wordNumber) {
        return wordSource.getWordForNumber(wordNumber).hashCode();
    }

    public String getStatistics() {
        return "No statistics";
    }

    public String getName() {
        return "Simple processor";
    }
}
