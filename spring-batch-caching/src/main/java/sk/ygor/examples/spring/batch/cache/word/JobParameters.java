package sk.ygor.examples.spring.batch.cache.word;

public class JobParameters {

    private final int wordLength;
    private final int columnCount;
    private final int stepSize;
    private final int stepCount;
    private final int maximumCacheSize;
    private final int expectedCheckSum;

    public JobParameters(int wordLength, int columnCount, int stepSize, int stepCount, int maximumCacheSize, int expectedCheckSum) {
        this.wordLength = wordLength;
        this.columnCount = columnCount;
        this.stepSize = stepSize;
        this.stepCount = stepCount;
        this.maximumCacheSize = maximumCacheSize;
        this.expectedCheckSum = expectedCheckSum;
    }

    public int getWordLength() {
        return wordLength;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getStepSize() {
        return stepSize;
    }

    public int getStepCount() {
        return stepCount;
    }

    public int getMaximumCacheSize() {
        return maximumCacheSize;
    }

    public int getExpectedCheckSum() {
        return expectedCheckSum;
    }
}
