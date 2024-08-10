package org.example.kafkatest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"kafka.*"})
@EntityScan({"kafka.*"})
@EnableJpaRepositories({"kafka.*"})
public class KafkaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTestApplication.class, args);
    }

}
