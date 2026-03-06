package com.example.Repository;

import com.example.models.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IAccountRepository  extends JpaRepository<Account,Long> {
    //Special method for multithreading safety
    //PESSIMISTIC_WRITE tells MSSQL to lock this record until the transactin ends
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE  a.id = :id")
    Optional<Account> findByIdWithLock(Long id);

    /*
    Why the Lock?
        In a multithreaded bank app, if two threads read
        the same balance at the same time, they might both try
         to subtract money simultaneously. findByIdWithLock ensures
         that only one thread can "touch" the account at a time
         at the database level.
     */
}
