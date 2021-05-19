package com.oidc.zunsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ZunsiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZunsiApplication.class, args);
    }

}
