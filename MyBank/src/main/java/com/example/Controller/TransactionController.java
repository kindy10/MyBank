package com.example.Controller;

import com.example.Service.TransactionService;
import com.example.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>>getHistory(@PathVariable Long accountId){
        return ResponseEntity.ok(transactionService.getHistoryByAccountId(accountId));
    }


}
