package org.rso.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public LocationMap locationMap(){
        return new LocationMap();
    }
}
