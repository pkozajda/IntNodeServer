package org.rso.config;

import org.rso.tasks.JobTask;
import org.rso.tasks.JobTaskExecutorService;
import org.rso.utils.JobQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public LocationMap locationMap(){
        return new LocationMap();
    }

    @Bean
    public JobQueue jobQueue(){
        return new JobQueue();
    }


    @Bean
    public JobTaskExecutorService jobTaskExecutorService(){
        JobTaskExecutorService jobTaskExecutorService = new JobTaskExecutorService(5,10,5000);
        jobTaskExecutorService.addNewTask(new JobTask("kamil",jobQueue()));
        jobTaskExecutorService.addNewTask(new JobTask("rafal",jobQueue()));
        jobTaskExecutorService.addNewTask(new JobTask("maks",jobQueue()));
        return jobTaskExecutorService;
    }
}
