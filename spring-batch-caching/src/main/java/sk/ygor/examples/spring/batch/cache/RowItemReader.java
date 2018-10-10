package sk.ygor.examples.spring.batch.cache;

import org.springframework.batch.item.ItemReader;
import sk.ygor.examples.spring.batch.cache.row.Row;
import sk.ygor.examples.spring.batch.cache.row.RowGenerator;

class RowItemReader implements ItemReader<Row> {

    private final int rowCountTotal;
    private final RowGenerator rowGenerator;

    private int rowsRead = 0;

    public RowItemReader(int stepSize, int stepCount, int columnCount) {
        this.rowCountTotal = stepSize * stepCount;
        this.rowGenerator = new RowGenerator(columnCount);
    }

    @Override
    public Row read() {
        if (rowsRead < rowCountTotal) {
            return rowGenerator.generateRow(rowsRead++);
        } else {
            return null;
        }
    }

}
