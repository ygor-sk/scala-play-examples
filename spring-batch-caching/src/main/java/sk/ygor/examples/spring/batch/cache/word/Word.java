package sk.ygor.examples.spring.batch.cache.word;

import java.io.Serializable;
import java.util.Arrays;

public class Word implements Serializable {

    public final char[] characters;

    public Word(char[] characters) {
        this.characters = characters;
    }

    @Override
    public String toString() {
        return new String(characters);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return Arrays.equals(characters, ((Word) o).characters);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(characters);
    }
}
