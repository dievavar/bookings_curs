package com.example.demo;
//класс для запуска приложения http://127.0.0.1:8080

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class BookingManagerApplication extends SpringBootServletInitializer{

    public static void main(String[] args){
        SpringApplication.run(BookingManagerApplication.class, args);
    }
}
