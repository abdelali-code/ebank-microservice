package org.enset.customerservice.services;


import lombok.RequiredArgsConstructor;
import org.enset.customerservice.entities.Customer;
import org.enset.customerservice.repositories.CustomerRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;



    @McpTool(description = "Get All Customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }



    @McpTool(description = "Get customer by id")
    public Customer getCustomerById(@McpToolParam(description = "customer id") Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }



    @McpTool(description = "save a new customer")
    public Customer saveCustomer(@McpToolParam(description = "the customer to save") Customer customer) {
        return customerRepository.save(customer);
    }

}
