package kr.co.oauthtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class OauthTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthTestApplication.class, args);
    }

}
