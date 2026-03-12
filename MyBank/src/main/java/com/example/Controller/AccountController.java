package com.example.Controller;

import com.example.Service.AccountService;
import com.example.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccount(){return ResponseEntity.ok(accountService.getAllAccounts());}

    @GetMapping("/{id}")
    public ResponseEntity<Account>getAccountById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account){
        return new ResponseEntity<>(accountService.saveAccount(account), HttpStatus.CREATED);
    }

    //---BANKING OPERATIONS------
    @PostMapping("/{id}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Long id,@RequestParam BigDecimal amount){
        accountService.deposit(id,amount);
        return ResponseEntity.ok("Deposit successful");
    }
    @PostMapping("/{id}/witdraw")
    public ResponseEntity<String>withdraw(@PathVariable Long id,@RequestParam BigDecimal amount){
        accountService.withdraw(id,amount);
        return ResponseEntity.ok("withdrawal successful");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String>transfer(@RequestParam Long fromId,
                                          @RequestParam Long toId,
                                          @RequestParam BigDecimal amount){
        accountService.transfer(fromId,toId,amount);
        return ResponseEntity.ok("Transfer of " + amount + " successful");
    }

    //Stress test
    @PostMapping("/stress-tess")
    public ResponseEntity<String> runStressTest(@RequestParam Long accountId){
        var executor = java.util.concurrent.Executors.newFixedThreadPool(10);
        BigDecimal amountPerWithdrawal = new BigDecimal("100.00");

        for(int i = 0; i<10; ++i){
            executor.submit(()->{
                try{
                    accountService.withdraw(accountId,amountPerWithdrawal);
                    System.out.println("Withdrawal successful by thread: " + Thread.currentThread().getName());
                }catch(Exception e){
                    System.err.println("Transaction failed: " + e.getMessage());
                }
            });
        }
        executor.shutdown();
        return ResponseEntity.ok("Stress test lauched with 10 simultaneous threads !!");

    }



}
