Singapore HDB Resale Analytics Platform
A Spring Boot based analytics platform integrating Singapore HDB open data, machine learning price prediction, and blockchain-inspired Merkle Tree verification.

Project Overview
This project automatically collects HDB resale transaction data from data.gov.sg, stores it in a local H2 database, provides historical transaction queries, predicts resale prices using Multiple Linear Regression, and verifies data integrity through SHA-256 Merkle Trees.
Key Features
Automatic HDB resale transaction collection
Scheduled daily synchronization
H2 local database persistence
Historical transaction query APIs
Multiple Linear Regression price prediction
Merkle Tree integrity verification
RESTful APIs with Spring Boot
System Architecture

data.gov.sg API
      |
HdbDataFetchService
      |
   H2 Database
  /     |      \
History Regression Merkle Tree
      \   |   /
     Spring Boot REST API
            |
        Frontend

Technology Stack
Layer	Technology
Backend	Spring Boot 3
Language	Java 21
Database	H2
ORM	Spring Data JPA
ML	Apache Commons Math (OLS)
Blockchain	SHA-256 + Merkle Tree
Build	Maven
Project Structure
src/main/java/com/uraapi
├── controller
├── entity
├── repository
├── service
└── UraApiApplication.java

resources/
├── application.properties
└── static/index.html
REST APIs
POST /api/fetch — Fetch latest HDB transactions
GET /api/transactions — Query by town
GET /api/transactions/recent — Recent transactions
GET /api/transactions/chain — Historical chain
POST /api/model/train — Train regression model
GET /api/model/predict — Predict resale price
POST /api/blockchain/build — Build daily Merkle Root
GET /api/blockchain/verify/{sourceId} — Verify transaction
Machine Learning
Features: Floor Area, Storey Midpoint, Mature Estate Flag.
Algorithm: Ordinary Least Squares Multiple Linear Regression.
Blockchain Verification
Each day's fetched transactions are hashed using SHA-256 to generate leaf nodes. A Merkle Tree is built, the root hash is stored in the database, and any transaction can later be verified by recomputing the proof path.
Future Improvements
PostgreSQL
Docker
React Frontend
Random Forest/XGBoost
Smart Contract integration