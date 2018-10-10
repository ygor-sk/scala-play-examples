package sk.ygor.examples.spring.batch.cache.word;

public interface WordSource {

    Word getWordForNumber(int wordNumber);

    String getStatistics();

    String getName();

}
