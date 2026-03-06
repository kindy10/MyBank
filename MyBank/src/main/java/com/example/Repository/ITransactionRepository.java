package com.example.Repository;

import com.example.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findBySourceAccountIdorDestinationAccountId(Long sourceId,Long destId);
}
