package com.github.accessreport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

@Configuration
public class AppConfig {

    // RestTemplate bean used to call external APIs (GitHub API)
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    // thread pool used to fetch contributors from multiple repositories in parallel
    @Bean
    public ExecutorService executorService(){

        return new ThreadPoolExecutor(
                5,                // core threads
                10,               // max threads
                60,               // idle timeout
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100)
        );
    }

}
