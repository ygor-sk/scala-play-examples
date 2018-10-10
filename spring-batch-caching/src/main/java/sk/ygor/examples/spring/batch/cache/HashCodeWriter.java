package sk.ygor.examples.spring.batch.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class HashCodeWriter implements ItemWriter<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(HashCodeWriter.class);

    private int checksum = 0;
    private int rowsWritten = 0;

    public void write(List<? extends Integer> hashCodes) {
//        logger.info(String.format("Writing %d hashcodes", hashCodes.size()));
        for (Integer hashCode : hashCodes) {
            checksum += hashCode;
        }
        rowsWritten += hashCodes.size();
    }

    public int getChecksum() {
        return checksum;
    }

    public int getRowsWritten() {
        return rowsWritten;
    }

}
