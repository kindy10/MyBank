package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data // automatic  Getter and Setter , toString......
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    public User(String firstName,String lastName,String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
