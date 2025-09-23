package com.semocompany.subsiteconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@SpringBootApplication
@EnableConfigServer
public class SubSiteConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubSiteConfigServerApplication.class, args);
    }

}
