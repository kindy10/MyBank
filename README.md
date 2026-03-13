# MyBank
🏦 Bank Management System (Spring Boot & MSSQL)
A robust banking application built with Spring Boot, using MSSQL Server for data persistence. This project focuses on transaction integrity through Pessimistic Locking, Optimistic Locking, and Multi-threading safety.

🚀 Key Features
User Management: Create and manage customer profiles.

Account Management: Support for multiple bank accounts per user.

Banking Operations: Real-time Deposit, Withdrawal, and Transfers.

Transaction History: Full audit trail for every movement of money.

Concurrency Control: Uses PESSIMISTIC_WRITE and @Version to prevent race conditions during simultaneous transactions.

Data Seeding: Automatically initializes some unique users and accounts on startup for testing.
## Project Overview
![MyBank Screenshot](Images/img.png)

🛠️ Technical Stack
Backend: Java 25, Spring Boot 4+, Spring Data JPA.

Database: Microsoft SQL Server (MSSQL).

Validation: Jakarta Bean Validation (Hibernate Validator).

Utilities: Lombok, Jackson (JSON Serialization).

⚙️ Setup & Configuration
1. Database Configuration
   Ensure your MSSQL instance is running and create a database named MyBank. Update your src/main/resources/application.properties:

2. Run the Application
   Bash
   mvn clean install
   mvn spring-boot:run
   📍 API Endpoints
   Users (/api/users)
   GET /api/users - List all users and their linked accounts.

POST /api/users - Create a new user profile.

Accounts (/api/accounts)
GET /api/accounts - List all accounts and current balances.

POST /api/accounts/{id}/deposit?amount=500 - Deposit money into an account.

POST /api/accounts/{id}/withdraw?amount=200 - Withdraw money from an account.

POST /api/accounts/transfer?fromId=1&toId=2&amount=1000 - Transfer money between two accounts.

Transactions (/api/transactions)
GET /api/transactions/statement/{accountId} - View the full bank statement (history) for a specific account.

Stress Testing
POST /api/accounts/stress-test?accountId=1 - Triggers 10 simultaneous threads to withdraw $100 each from Account #1 to test database locking mechanisms.

🧪 Concurrency & Safety
The application ensures data integrity using two layers of protection:

Pessimistic Locking: Prevents two transactions from reading the same balance at the same time.

Java
@Lock(LockModeType.PESSIMISTIC_WRITE)
Optional<Account> findById(Long id);
Optimistic Locking: Uses a @Version column in MSSQL to detect if a row was modified by another thread before saving.
