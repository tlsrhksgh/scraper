package com.single.project.scraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@SpringBootApplication
public class ScrapperApplication {

    public static void main(String[] args) {

        SpringApplication.run(ScrapperApplication.class, args);

        System.out.println("Main -> " + Thread.currentThread().getName());
    }
}
