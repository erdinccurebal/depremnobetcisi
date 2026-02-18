package com.depremnobetcisi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DepremNobetcisiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepremNobetcisiApplication.class, args);
    }
}
