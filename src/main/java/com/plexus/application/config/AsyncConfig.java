package com.plexus.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        // Usar Virtual Threads de Java 21
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}


