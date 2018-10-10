package sk.ygor.examples.spring.batch.cache;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowJob;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.ygor.examples.spring.batch.cache.facade.GuavaCacheFacade;
import sk.ygor.examples.spring.batch.cache.row.Row;
import sk.ygor.examples.spring.batch.cache.word.ExampleParameters;
import sk.ygor.examples.spring.batch.cache.word.WordSource;

import java.util.function.Function;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    ExampleParameters exampleParameters;

    @Autowired
    WordSourceFactory wordSourceFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job wordChecksumJob() {
        int maximumCacheSize = exampleParameters.getMaximumCacheSize();
        return jobBuilderFactory.get("warm up")
                .incrementer(new RunIdIncrementer())
                .start(dummyStep())
//                .next(createStep(wordSourceFactory.plain(), ChecksumItemProcessor::new))
//                .next(createStep(wordSourceFactory.cachedBySpring(), ChecksumItemProcessor::new))
//                .next(createStep(wordSourceFactory.cachedByHashMap(), ChecksumItemProcessor::new))
//                .next(createStep(wordSourceFactory.cachedByLinkedHashMap(maximumCacheSize), ChecksumItemProcessor::new))
//                .next(createStep(wordSourceFactory.cachedByGuava(maximumCacheSize), ChecksumItemProcessor::new))
//                .next(createStep(wordSourceFactory.cachedByEhCache(maximumCacheSize), ChecksumItemProcessor::new))
//                .next(createStep(wordSourceFactory.plain(), wordSource -> new ChecksumItemProcessorCached(wordSource, new GuavaCacheFacade<>(maximumCacheSize * 20))))
                .next(createStep(wordSourceFactory.cachedByHashMapSoftReference(), ChecksumItemProcessor::new))
                .build();
    }

    private Step createStep(WordSource wordSource, Function<WordSource, ChecksumItemProcessor> processorFactory) {
        RowItemReader reader = new RowItemReader(exampleParameters.getStepSize(), exampleParameters.getStepCount(), exampleParameters.getColumnCount());
        ChecksumItemProcessor processor = processorFactory.apply(wordSource);
        HashCodeWriter writer = new HashCodeWriter();
        StepExecutionListener listener = new StepExecutionListener(wordSource, processor, writer, exampleParameters.getExpectedCheckSum());

        return stepBuilderFactory.get("step-" + wordSource.getName() + "-" + processor.getName())
                .<Row, Integer>chunk(exampleParameters.getStepSize())
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
