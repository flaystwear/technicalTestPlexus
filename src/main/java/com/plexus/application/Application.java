package com.plexus.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.plexus")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


