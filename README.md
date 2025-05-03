# 🌀 BatchProject

A Spring Boot + Spring Batch project that periodically fetches issues from the Redmine API and stores them in a PostgreSQL database.

## 📌 Project Purpose

The goal is to collect assigned and closed Redmine issues within a certain time interval and save them into a database for further reporting or processing.

## 🚀 Technologies Used

- Java 17  
- Spring Boot  
- Spring Batch  
- Spring Data JPA (Hibernate)  
- PostgreSQL  
- Maven  
- dotenv-java (for environment variables)  
- Redmine REST API  

## 🛠 Setup Instructions

🧠 How It Works
Connects to Redmine using the API key.

Filters issues that are assigned to a user and have a closed status.

Filters by closed_date within a configurable time range.

Saves the filtered issues into the PostgreSQL database.

Optionally sends email notifications or reports.

🔐 Security Notice
Keep your .env file private — it should never be committed to version control.

Store sensitive credentials securely (e.g., using environment variables in production).
