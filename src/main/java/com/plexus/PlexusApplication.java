package com.plexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.plexus")
public class PlexusApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlexusApplication.class, args);
    }
}


