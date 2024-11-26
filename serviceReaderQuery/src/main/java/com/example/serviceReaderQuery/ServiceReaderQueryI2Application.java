package com.example.serviceReaderQuery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class ServiceReaderQueryI2Application {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "i2, bootstrap");
        SpringApplication.run(ServiceReaderQueryI2Application.class, args);
    }
}
