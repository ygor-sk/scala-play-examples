package sk.ygor.examples.spring.batch.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import sk.ygor.examples.spring.batch.cache.word.ExampleParameters;

@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ExampleParameters scenario() {
        return new ExampleParameters(
                128, 6,
                100000, 20,
                200000,
                451465944
        );
    }

}
