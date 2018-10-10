package sk.ygor.examples.spring.batch.cache.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WordGenerator implements WordSource {

    @Autowired
    private ExampleParameters exampleParameters;

    @Override
    public Word getWordForNumber(int wordNumber) {
        Random random = new Random(wordNumber);
        char[] chars = new char[exampleParameters.getWordLength()];
        for (int j = 0; j < chars.length; j++) {
            chars[j] = (char) (random.nextInt(26) + 'a');
        }
        return new Word(chars);
    }

    @Override
    public String getStatistics() {
        return "No statistics";
    }

    @Override
    public String getName() {
        return "Plain generator";
    }
}
