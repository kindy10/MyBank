package com.example.Config;

import com.example.Repository.IAccountRepository;
import com.example.Repository.IUserRepository;
import com.example.models.Account;
import com.example.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(IUserRepository userRepository, IAccountRepository accountRepository) {
        return args -> {
            //Check if data already exists to avoid duplicates in MSSQL
            if (userRepository.count() == 0) {
                System.out.println("--- Seeding some Users and  Accounts ---");
                List<String> firstNames = List.of("Kindy", "Mamadou", "Abdoul", "Amadou", "Marc", "Fode", "Moustapha", "Bangaly", "Pape", "Ammar", "Patrice", "Filon", "Navy");
                List<String> lastNames = List.of("Bah", "Balde", "Diakite", "Diau", "Coulibaly", "Socgfak", "Sylla", "Camara", "MBaye", "Bah", "Snwoui", "MvB", "Junior");

                for (int i = 0; i < 13; ++i) {
                    User user = new User(firstNames.get(i), lastNames.get(i), firstNames.get(i).toLowerCase() + "." + lastNames.get(i).toLowerCase() + "@bank.com");
                    userRepository.save(user); //Save to get the ID

                    //2. Create and persist the Account linked to that User
                    Account account = new Account(user, new BigDecimal("10000.00"));
                    accountRepository.save(account);
                }
                System.out.println("--- Seeding Complete: 13 Users and Accounts created in MSSQL ---");
            } else System.out.println("--- Database already contains data. Skipping seeding. ---");
        };
    }
}
