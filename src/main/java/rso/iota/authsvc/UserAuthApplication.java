package rso.iota.authsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class UserAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAuthApplication.class, args);
    }
}
