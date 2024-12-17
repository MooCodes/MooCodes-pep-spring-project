package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account findById(Integer id) {
        return accountRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                String.format("User with id %d not found", id)
            )
        );
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
