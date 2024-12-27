package com.example.serviceRecomCom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class ServiceRecomI1Application {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "i1, bootstrap");
        SpringApplication.run(ServiceRecomI1Application.class, args);
    }
}
