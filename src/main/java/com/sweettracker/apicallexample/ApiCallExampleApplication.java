package com.sweettracker.apicallexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiCallExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiCallExampleApplication.class, args);
    }

}
