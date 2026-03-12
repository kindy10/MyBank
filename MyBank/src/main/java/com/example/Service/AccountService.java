package com.example.Service;

import com.example.Repository.IAccountRepository;
import com.example.Repository.ITransactionRepository;
import com.example.models.Account;
import com.example.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private ITransactionRepository transactionRepository;

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }
    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElseThrow();
    }
    public Account saveAccount(Account account){return accountRepository.save(account);}
    public void deleteAccount(Long id){accountRepository.deleteById(id);}

    /**
     * Executes a money transfer between two accounts.
     * The @Transactional annotation ensures that if any part fails,
     * the entire operation is canceled in MSSQL.
     */
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount){
        if(fromAccountId.equals(toAccountId))
            throw new RuntimeException("Cannot transfer money to the same account");

        /*Race Condition: Two people editing the same document at the same
            time -> final version depends on who saves last.
        Deadlock: Two people trying to pass each other in a narrow hallway , but both refuse to move
            -> nobody moves at all.
            Race condition: wrong results due to timing.
            Deadlock:  no results due to waiting forever.

            A lock is a synchronization primative that ensures only one thread/process can
                access a shared resource at  time.It prevents race conditions by enforcing
                mutual exclusion.However ,improper use of locks can lead to deadlocks.

                //---DEAD;OCK PREVENTION LOGIC----
                //Always lock the smaller ID first, then the larger ID
                //this ensures every thread follows the same "path"

         */
        Long firstId = Math.min(fromAccountId,toAccountId);
        Long secondId = Math.max(fromAccountId,toAccountId);

        Account firstAccount = accountRepository.findByIdWithLock(firstId)
                .orElseThrow(()-> new RuntimeException("Account not found: " + firstId));
        Account secondAccount = accountRepository.findByIdWithLock(secondId)
                .orElseThrow(()-> new RuntimeException("Account not found: " + secondId));

        //Re-identify which one is 'from ' and 'to' for the business logic
        Account fromAccount = (fromAccountId.equals(firstId) ) ? firstAccount : secondAccount ;
        Account toAccount = (fromAccountId.equals(firstId) ) ? firstAccount : secondAccount ;

        //2.Validate balance
        if(fromAccount.getBalance().compareTo(amount)< 0)
            throw new RuntimeException("Insufficient funds on account: "+ fromAccountId);

        //3.Perorm the math
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        //4.save updated balances
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        //5.Log the transaction in the history table
        Transaction trans = new Transaction(fromAccount,toAccount,amount,"Transfer");
        transactionRepository.save(trans);

        System.out.println("Transfer successful: "+ amount + "from "+ fromAccountId + " to "+ toAccountId);
    }
    /**
     * Deposits money into an account.
     *Thread-safe  via Pessimistic Locking.
     */

    @Transactional
    public void deposit(Long accountId,BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Deposit amount must be positive");
        //Lock the account so no one else changes the balance during deposit

        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        //Record the history
        Transaction trans = new Transaction(null,account,amount, "DEPOSIT");
        transactionRepository.save(trans);
    }
    /**
     * Withdraws money from an account
     * Thread-safe via Pessimistic Locking.
     */
    @Transactional
    public void withdraw (Long accountId,BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Withdrawal amount must be positive");
        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        //check if enough funds exist

        if (account.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient funds for withdrawal on account "+ accountId);
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        //Record history
        Transaction trans = new Transaction(account,null,amount,"WITHDRAWAL");
        transactionRepository.save(trans);
        System.out.println("Withdrew " + amount + " from account " + accountId);
    }
    /**
     * Get current balance (No lock needed for just reading,usually)
     */
    public BigDecimal getBalance(Long accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not fount"));
        return account.getBalance();
    }
}
