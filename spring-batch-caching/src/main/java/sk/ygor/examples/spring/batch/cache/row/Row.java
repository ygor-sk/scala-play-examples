package sk.ygor.examples.spring.batch.cache.row;

public class Row {

    private final int[] wordNumbers;

    public Row(int[] wordNumbers) {
        this.wordNumbers = wordNumbers;
    }

    public int[] getWordNumbers() {
        return wordNumbers;
    }
}
