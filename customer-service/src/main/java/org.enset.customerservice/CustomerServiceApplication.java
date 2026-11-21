package org.enset.customerservice;

import org.enset.customerservice.entities.Customer;
import org.enset.customerservice.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}




	@Bean
	CommandLineRunner commandLineRunner(CustomerService customerService) {
		return args -> {
			List<String> names = List.of("mohamed", "imane", "anas");
			names.forEach(nm -> customerService.saveCustomer(Customer.builder()
					.nom(nm)
					.email(nm + "@gmail.com")
					.build()));
		};
	}

}
