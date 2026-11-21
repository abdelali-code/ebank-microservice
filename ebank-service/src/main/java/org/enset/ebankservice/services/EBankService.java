package org.enset.ebankservice.services;


import lombok.RequiredArgsConstructor;
import org.enset.ebankservice.entities.BankAccount;
import org.enset.ebankservice.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EBankService {

    private final BankAccountRepository bankAccountRepository;

    public List<BankAccount> getAllBankAccount() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getBankAccountById(String bankId) {
        return bankAccountRepository.findById(bankId)
                .orElseThrow(() -> new RuntimeException("account not found"));
    }

    public BankAccount saveBankAccountById(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }
}
