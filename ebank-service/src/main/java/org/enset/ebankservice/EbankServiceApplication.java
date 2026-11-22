package org.enset.ebankservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

}
