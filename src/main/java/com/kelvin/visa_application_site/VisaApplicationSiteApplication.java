package com.kelvin.visa_application_site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VisaApplicationSiteApplication {

    // Todo : Create a feature to filter the application by the visaType and send mail according to that category
    // Todo : Work on the support user endpoint

    public static void main(String[] args) {
        SpringApplication.run(VisaApplicationSiteApplication.class, args);
        System.out.println("✅ Application Started, Test your Endpoints  !!");
    }

}
