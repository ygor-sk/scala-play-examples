package sk.ygor.examples.spring.batch.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import sk.ygor.examples.spring.batch.cache.word.WordSource;

class StepExecutionListener extends StepExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(StepExecutionListener.class);

    private final WordSource wordSource;
    private final ChecksumProcessor processor;
    private final CheckSumWriter writer;
    private final int expectedCheckSum;

    private long start;

    public StepExecutionListener(WordSource wordSource, ChecksumProcessor processor, CheckSumWriter writer, int expectedCheckSum) {
        this.expectedCheckSum = expectedCheckSum;
        this.wordSource = wordSource;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        start = System.currentTimeMillis();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long duration = System.currentTimeMillis() - start;
        double speed = 1000.0 * writer.getRowsWritten() / duration;
        logger.info("-----------------------------------------------------------------------");
        logger.info(String.format("Finished step. Word source: %s. Processor: %s", wordSource.getName(), processor.getName()));
        logger.info(String.format("Word source statistics: %s", wordSource.getStatistics()));
        logger.info(String.format("Processor statistics: %s", processor.getStatistics()));
        logger.info(String.format("Speed: %.2f rows/sec", speed));
        if (expectedCheckSum == writer.getChecksum()) {
            logger.info("Checksum: OK");
        } else {
            logger.info(String.format("Checksum: WRONG. Expected checksum: %d. Actual checksum: %d", expectedCheckSum, writer.getChecksum()));
        }
        logger.info("-----------------------------------------------------------------------");
        return super.afterStep(stepExecution);
    }

}
