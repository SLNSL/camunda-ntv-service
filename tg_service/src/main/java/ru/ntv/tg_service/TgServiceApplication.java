package ru.ntv.tg_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import ru.ntv.tg_service.dto.kafka.ArticleKafkaDTO;

@SpringBootApplication
public class TgServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgServiceApplication.class, args);
    }


}