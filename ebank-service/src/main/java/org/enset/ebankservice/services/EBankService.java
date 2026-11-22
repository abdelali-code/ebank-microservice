package org.enset.ebankservice.services;


import lombok.RequiredArgsConstructor;
import org.enset.ebankservice.entities.BankAccount;
import org.enset.ebankservice.feign.CustomerRestClient;
import org.enset.ebankservice.model.Customer;
import org.enset.ebankservice.repositories.BankAccountRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EBankService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRestClient customerRestClient;



    @McpTool(description = "get all bank account")
    public List<BankAccount> getAllBankAccount() {
        return bankAccountRepository.findAll();
    }


    @McpTool(description = "get bank account by id")
    public BankAccount getBankAccountById(@McpToolParam(description = "bank account id") String bankId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankId)
                .orElseThrow(() -> new RuntimeException("account not found"));
        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    @McpTool(description = "add a new bank account")
    public BankAccount saveBankAccountById(@McpToolParam(description = "the bank account to save (balance, type,customerId)") BankAccount bankAccount) {
        try {
            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
            return bankAccountRepository.save(bankAccount);
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
