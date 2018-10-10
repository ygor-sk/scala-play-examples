package sk.ygor.examples.spring.batch.cache;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.ygor.examples.spring.batch.cache.facade.GuavaCacheFacade;
import sk.ygor.examples.spring.batch.cache.row.Row;
import sk.ygor.examples.spring.batch.cache.word.JobParameters;
import sk.ygor.examples.spring.batch.cache.word.WordSource;

import java.util.function.Function;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    JobParameters jobParameters;

    @Autowired
    WordSourceFactory wordSourceFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job wordChecksumJob() {
        int maximumCacheSize = jobParameters.getMaximumCacheSize();
        return jobBuilderFactory.get("wordChecksumJob")
                .incrementer(new RunIdIncrementer())
                .start(dummyStep())
                .next(createStep("Plain", wordSourceFactory.plain(), ChecksumProcessor::new))
                .next(createStep("Spring cached", wordSourceFactory.cachedBySpring(), ChecksumProcessor::new))
                .next(createStep("HashMap", wordSourceFactory.cachedByHashMap(), ChecksumProcessor::new))
                .next(createStep("LinkedHashMap", wordSourceFactory.cachedByLinkedHashMap(maximumCacheSize), ChecksumProcessor::new))
                .next(createStep("Guava", wordSourceFactory.cachedByGuava(maximumCacheSize), ChecksumProcessor::new))
                .next(createStep("EhCache", wordSourceFactory.cachedByEhCache(maximumCacheSize), ChecksumProcessor::new))
                .next(createStep("Guava(smart)", wordSourceFactory.plain(), wordSource -> new ChecksumProcessorCached(wordSource, new GuavaCacheFacade<>(maximumCacheSize * 20))))
                .next(createStep("HashMap(soft)", wordSourceFactory.cachedByHashMapSoftReference(), ChecksumProcessor::new))
                .build();
    }

    private Step createStep(String stepName, WordSource wordSource, Function<WordSource, ChecksumProcessor> processorFactory) {
        RowItemReader reader = new RowItemReader(jobParameters.getStepSize(), jobParameters.getStepCount(), jobParameters.getColumnCount());
        ChecksumProcessor processor = processorFactory.apply(wordSource);
        CheckSumWriter writer = new CheckSumWriter();
        StepExecutionListener listener = new StepExecutionListener(wordSource, processor, writer, jobParameters.getExpectedCheckSum());

        return stepBuilderFactory.get(stepName)
                .<Row, Integer>chunk(jobParameters.getStepSize())
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(listener)
                .build();
    }

    private Step dummyStep() {
        return stepBuilderFactory.get("dummy")
                .<String, String>chunk(1)
                .reader(() -> null)
                .processor(new ItemProcessorAdapter<>())
                .build();
    }

}
