package org.enset.customerservice.services;


import lombok.RequiredArgsConstructor;
import org.enset.customerservice.entities.Customer;
import org.enset.customerservice.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }



    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
