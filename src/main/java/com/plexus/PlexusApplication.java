package com.plexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.plexus")
@EnableAsync
public class PlexusApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlexusApplication.class, args);
    }
}


