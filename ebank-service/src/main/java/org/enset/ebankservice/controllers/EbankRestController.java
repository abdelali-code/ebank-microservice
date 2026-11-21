package org.enset.ebankservice.controllers;


import lombok.RequiredArgsConstructor;
import org.enset.ebankservice.entities.BankAccount;
import org.enset.ebankservice.services.EBankService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ebank")
@RequiredArgsConstructor
public class EbankRestController {

    private final EBankService eBankService;


    @GetMapping("")
    public List<BankAccount> getAllBankAccount() {
        return eBankService.getAllBankAccount();
    }

    @GetMapping("/{id}")
    public BankAccount getBankAccountById(@PathVariable("id") String bankId) {
        return eBankService.getBankAccountById(bankId);
    }


    @PostMapping("")
    public BankAccount saveBankAccountById(@RequestBody BankAccount bankAccount) {
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        return eBankService.saveBankAccountById(bankAccount);
    }
}
