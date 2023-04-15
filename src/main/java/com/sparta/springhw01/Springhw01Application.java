package com.sparta.springhw01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Springhw01Application {

    public static void main(String[] args) {
        SpringApplication.run(Springhw01Application.class, args);
    }

}
