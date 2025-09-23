package com.semocompany.subsiteeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SubSiteEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubSiteEurekaServerApplication.class, args);
    }

}
