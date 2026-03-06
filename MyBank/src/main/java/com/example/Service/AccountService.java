package com.example.Service;

import com.example.Repository.IAccountRepository;
import com.example.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private IAccountRepository accountRepository;

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }
    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElseThrow();
    }
    public Account saveAccount(Account account){return accountRepository.save(account);}
    public void deleteAccount(Long id){accountRepository.deleteById(id);}
}
