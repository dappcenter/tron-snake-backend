package de.lj.tronsnakebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class TronSnakeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TronSnakeBackendApplication.class, args);
    }

}
