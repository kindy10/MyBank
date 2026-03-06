package com.example.Service;

import com.example.Repository.ITransactionRepository;
import com.example.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private ITransactionRepository transactionRepository;

    public List<Transaction> getHistoryByAccountId(Long accountId){
        return transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId,accountId);
    }

}
