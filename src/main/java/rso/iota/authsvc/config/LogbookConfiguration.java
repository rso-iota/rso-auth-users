package rso.iota.authsvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;

@Configuration
public class LogbookConfiguration {
    @Bean
    public Logbook logbook() {
        return Logbook.builder().build();
    }
}