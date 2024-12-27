package com.example.serviceRecomCom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class ServiceRecomI2Application {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "i2, bootstrap");
        SpringApplication.run(ServiceRecomI2Application.class, args);
    }
}
