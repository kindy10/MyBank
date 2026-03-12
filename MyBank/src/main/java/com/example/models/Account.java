package com.example.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message="Balance must be positive")
    private BigDecimal balance;

    //Link accounts to a User
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    //Optimistic locking: Prevents two threads from updating the same account simultaneously
    @Version
    private Long version;

    public Account(User owner,BigDecimal initialBalance){
        this.owner = owner;
        this.balance = initialBalance;
    }


}
