package com.example.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy ="owner",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Account> accounts;

    public User(String firstName,String lastName,String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
