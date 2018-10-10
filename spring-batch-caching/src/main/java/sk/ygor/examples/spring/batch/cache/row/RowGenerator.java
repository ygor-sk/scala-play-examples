package sk.ygor.examples.spring.batch.cache.row;

import java.util.Random;

public class RowGenerator {

    private final int columnCount;

    public RowGenerator(int columnCount) {
        this.columnCount = columnCount;
    }

    public Row generateRow(int rowNumber) {
        Random random = new Random(rowNumber);
        int[] wordNumbers = new int[columnCount];
        int columnCardinality = 10;
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            wordNumbers[columnIndex] = random.nextInt(columnCardinality);
            columnCardinality *= 10;
        }
        return new Row(wordNumbers);
    }
}
