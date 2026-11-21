package org.enset.customerservice.controllers;


import lombok.RequiredArgsConstructor;
import org.enset.customerservice.entities.Customer;
import org.enset.customerservice.services.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping("")
    public List<Customer> getALlCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("")
    public Customer saveCustomer(@RequestBody  Customer customer) {
        return customerService.saveCustomer(customer);
    }
}
