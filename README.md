## Finance Vault API

The backend service of the Finance Vault project — a personal finance tracking application.
It provides secure user authentication, transaction management, and financial summaries with modern backend practices.


🚀 Tech Stack

    Java 17

    Spring Boot 3

    Spring Security + JWT for authentication & authorization

    Spring Data JPA for ORM and query building

    PostgreSQL as the main relational database

    Maven for dependency management

    Docker for containerization and deployment

    Render for cloud hosting

### 🔑 Features

🔒 Security

    JWT-based Authentication for stateless and secure session management

    Custom authentication entry point for handling unauthorized access

    CORS configuration allowing frontend interaction

👤 User Management

    User registration with validation (email, password strength, etc.)

    User profile updates (name, email, currency)

    Automatic creation of demo transactions on registration for initial dashboard display

💰 Transaction Management

    Create, list, and manage transactions between users

    Transactions linked with sender and receiver relationships via JPA entity mapping

    Support for pagination, filtering, and sorting to handle large transaction histories

    Monthly summaries including total deposits, withdrawals, and balances

🗄️ Database

    PostgreSQL with schema managed through JPA entities

    Pre-configured to work with NeonDB (serverless PostgreSQL)

    Dockerized database setup for local development

### 📈 Future Improvements

    Enhanced role-based authorization (admin, user)

    Improved UI/UX integration with the frontend

    Unit & integration test coverage expansion

    Deployment with CI/CD pipelines
