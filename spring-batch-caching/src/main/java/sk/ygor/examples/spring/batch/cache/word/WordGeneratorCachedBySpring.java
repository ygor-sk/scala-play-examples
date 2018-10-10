package sk.ygor.examples.spring.batch.cache.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WordGeneratorCachedBySpring implements WordSource {

    private final WordGenerator wordGenerator;

    @Autowired
    public WordGeneratorCachedBySpring(WordGenerator wordGenerator) {
        this.wordGenerator = wordGenerator;
    }

    @Override
    @Cacheable("getWordForNumber")
    public Word getWordForNumber(int wordNumber) {
        return wordGenerator.getWordForNumber(wordNumber);
    }

    @Override
    public String getStatistics() {
        return wordGenerator.getStatistics();
    }

    @Override
    public String getName() {
        return "Cached by Spring";
    }
}
