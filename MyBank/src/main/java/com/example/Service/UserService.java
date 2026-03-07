package com.example.Service;

import com.example.Repository.IUserRepository;
import com.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    public List<User> getAllUsers(){return userRepository.findAll();}

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User "+ id + " not found"));
    }

    public User createUser(User user){
        //Simpe check : stop if email is already in Mssql

        if (userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email "+ user.getEmail() + " is already registered.");
        }
        return userRepository.save(user);
    }
    public void deleteUser(Long id){ userRepository.deleteById(id);}
}
