package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {

        SpringApplication.run(FilmorateApplication.class, args);
       // SpringApplication application = new SpringApplication(FilmorateApplication.class);


    }

}
