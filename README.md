# 🌀 Redmine Issue Tracker Batch Project

# 📌 Project Purpose & Project Overview
This project aims to automate the process of fetching and analyzing Redmine issue data to track and visualize employee performance. By utilizing a cron job mechanism, Redmine data is fetched regularly, processed, and presented in visually appealing charts and tables to give an overview of issues resolved by employees.

## 🚀 Technologies Used

Automated Data Fetching: Fetch Redmine issue data on a regular basis via cron jobs.

Data Analysis: Process the fetched data to track employee performance.

Custom Time Ranges: Specify time intervals (e.g., daily, weekly, monthly) to generate tailored reports.

Visual Reporting: Display data in intuitive graphs, charts, and tables.

Redmine API: To fetch real-time issue data.

Cron Jobs: To automate periodic data fetching.

Java: Backend data processing and Redmine integration.

Spring Boot: For creating backend services.

MySQL/PostgreSQL: For data storage.

Thymeleaf/JavaScript: For rendering visual charts.

Data Processing
The fetched data is processed to track metrics such as:

Number of issues resolved per employee

Time taken to resolve each issue

Trends over time

Reporting
The processed data is visualized as:

Graphs and charts showing the number of issues resolved

Tables summarizing team performance

Prerequisites
Redmine API Key: You need your Redmine API key.

MySQL/PostgreSQL: Database to store fetched data.

Java 8 or Later: To run the backend services.

Spring Boot: For backend services.


Optionally sends email notifications or reports.

🔐 Security Notice
Keep your .env file private — it should never be committed to version control.

Store sensitive credentials securely (e.g., using environment variables in production).
