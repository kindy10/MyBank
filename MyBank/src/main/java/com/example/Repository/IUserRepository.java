package com.example.Repository;

import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    //Spring will automatically generate the query to find user by email
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
