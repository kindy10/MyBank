package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //The account that initiated the transaction
    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    //The account that receiving the money (null for cas withdrawals)
    @ManyToOne
    @JoinColumn(name ="destination_account_id")
    private Account destinationAccount;
    private BigDecimal amount;
    //type of operation: TRANSFER,DEPOSIT,WITHDRAWAL

    private String type;
    private LocalDateTime timestamp;

    public Transaction(Account source,Account destination,BigDecimal amount,String type){
        this.sourceAccount = source;
        this.destinationAccount = destination;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

}
