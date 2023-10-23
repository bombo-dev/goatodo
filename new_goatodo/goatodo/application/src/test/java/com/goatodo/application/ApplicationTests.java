package com.goatodo.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.goatodo.domain"})
@EnableJpaRepositories(basePackages = {"com.goatodo.domain"})
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}
