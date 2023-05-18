package ru.ntv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class NTVApplication{
    public static void main(String[] args) {
        SpringApplication.run(NTVApplication.class, args);
    }
}