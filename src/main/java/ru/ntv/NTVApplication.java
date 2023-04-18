package ru.ntv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EntityScan
public class NTVApplication {
    public static void main(String[] args) {
        SpringApplication.run(NTVApplication.class, args);
    }
}