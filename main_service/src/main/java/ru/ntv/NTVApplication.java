package ru.ntv;


import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootApplication

public class NTVApplication{
    public static void main(String[] args) {
        SpringApplication.run(NTVApplication.class, args);
    }

//    @Bean
//    public NewTopic topic() {
//        return TopicBuilder.name("topic1")
//                .partitions(10)
//                .replicas(1)
//                .build();
//    }
//    @Bean
//    public ApplicationRunner runner(KafkaTemplate<String, String> template) {
//        return args -> {
//            template.send("topic1", "test");
//        };
//    }



}