package org.enset.gatawayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatawayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatawayServiceApplication.class, args);
    }

    // Dynamic routing: creates one route per service registered in Eureka,
    // e.g. /CUSTOMER-SERVICE/customers -> lb://CUSTOMER-SERVICE/customers
    @Bean
    DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient rdc,
                                                        DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }
}
