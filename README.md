# Redmine Dashboard Backend

## Project Overview

This project is a backend service that fetches data from the **Redmine** project management system, processes and stores it in a database, and provides RESTful APIs to serve this data to a React + Vite frontend application.

---

## Architecture & Data Flow

```plaintext
Redmine API
    ↓ (1. Data Fetching)
Backend Service (Spring Boot)
    - Retrieves issues, users, projects, etc. from Redmine API
    - Processes and transforms data
    - Persists data to relational database (MySQL/PostgreSQL)
    - Exposes REST API endpoints
        ↓ (2. API Data Serving)
React + Vite Frontend
    - Fetches data from backend APIs
    - Renders dashboards and visualizations


Step 1: Fetching Data from Redmine API
Data is fetched periodically or manually via scheduled batch services.

Data includes issues, projects, users, with custom business logic applied (e.g., overdue issues, user performance).

Transactions and error handling ensure data consistency.

Step 2: Persisting Data to Database
Data is saved and updated in the database via JPA/Hibernate repositories.

Efficient database operations ensure up-to-date and consistent records.

Step 3: Serving Data via REST API
RESTful endpoints implemented using Spring MVC (@RestController).

Frontend makes fetch requests to these endpoints and receives JSON responses.

Data is then used for dashboard charts, tables, and metrics.

Technologies Used


| Layer           | Technology            | Description                     |
| --------------- | --------------------- | ------------------------------- |
| Backend         | Java 17 + Spring Boot | API and business logic services |
| Data Access     | Hibernate + JPA       | Database interaction            |
| Database        | MySQL/PostgreSQL      | Persistent data storage         |
| Frontend        | React + Vite          | Modern, fast UI                 |
| Charts & Styles | Recharts + Tailwind   | Dashboard visualization         |
| Communication   | REST API + Fetch      | Backend-Frontend communication  |



API Endpoints Examples

| Endpoint                        | Method | Description                |
| ------------------------------- | ------ | -------------------------- |
| `/api/dashboard/stats`          | GET    | Returns general statistics |
| `/api/dashboard/top-performers` | GET    | Lists top performing users |
| `/api/dashboard/overdue-issues` | GET    | Lists overdue issues       |
| `/api/dashboard/most-active`    | GET    | Lists most active users    |
