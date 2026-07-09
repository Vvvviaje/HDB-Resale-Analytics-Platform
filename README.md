# Singapore HDB Resale Analytics Platform

A Spring Boot based data analytics platform for collecting, analysing and verifying Singapore HDB resale transactions using government open data, statistical learning and blockchain-inspired integrity verification.

---

## Project Overview

The Singapore HDB Resale Analytics Platform is a backend application developed using Spring Boot.

The system automatically retrieves HDB resale transaction data from the official Singapore Government Open Data API (data.gov.sg), stores the records in a local H2 database, provides historical transaction analytics, predicts resale prices using Multiple Linear Regression, and verifies transaction integrity through a SHA-256 based Merkle Tree.

Although this project focuses on real estate analytics, its architecture follows the concept of an **Information Supply Chain**, where raw government data is transformed into reliable business intelligence through multiple processing stages.

---

# System Architecture

```text
                    data.gov.sg Open Data API
                              │
                              ▼
                  HdbDataFetchService
             (Scheduled / Manual Synchronization)
                              │
                              ▼
                        H2 Database
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
 Historical Query     Price Regression      Merkle Tree
    Service               Service             Service
        └─────────────────────┼─────────────────────┘
                              ▼
                    Spring Boot REST APIs
                              │
                              ▼
                        Client / Frontend
```

The project follows a layered architecture consisting of:

- Controller Layer
- Service Layer
- Repository Layer
- Database Layer

This separation improves maintainability, scalability and future extensibility.

---

# Information Flow

```text
Government Open Data
          │
          ▼
Data Acquisition
          │
          ▼
Data Cleaning
          │
          ▼
Local Database
          │
          ▼
Historical Analytics
          │
          ▼
Machine Learning Prediction
          │
          ▼
Integrity Verification
          │
          ▼
Decision Support
```

Instead of directly analysing raw government data, the platform progressively transforms information into decision-support knowledge.

This processing pipeline resembles an **Information Supply Chain**, where data continuously flows through multiple value-added stages.

---

# Machine Learning Pipeline

```text
Historical Transactions
          │
          ▼
Feature Engineering
(Floor Area,
Storey Level,
Mature Estate)
          │
          ▼
OLS Multiple Linear Regression
          │
          ▼
Model Training
          │
          ▼
Price Prediction
```

The prediction model uses Ordinary Least Squares Multiple Linear Regression.

Independent variables include:

- Floor Area
- Storey Midpoint
- Mature Estate Indicator

Dependent variable:

- Resale Price

Model quality is evaluated using the coefficient of determination (R²).

---

# Merkle Tree Verification

```text
                    Root Hash
                  /           \
              Hash12         Hash34
             /     \        /      \
          Hash1   Hash2   Hash3   Hash4
            │        │       │        │
           Tx1      Tx2     Tx3      Tx4
```

To guarantee data integrity, every transaction is converted into a SHA-256 hash.

Transactions collected on the same day are grouped into a Merkle Tree.

The generated Root Hash is stored in the database for future verification.

Given any transaction, the system can reconstruct the Merkle Proof and verify whether the transaction has been modified.

---

# Supply Chain Perspective

Although the application analyses housing transactions, its system design follows an **Information Supply Chain**.

```text
Raw Government Data
        │
        ▼
Collection
        │
        ▼
Storage
        │
        ▼
Analytics
        │
        ▼
Integrity Verification
        │
        ▼
Decision Support
```

From a supply chain perspective:

- Government data is treated as the upstream information source.
- Data processing represents value-added transformation.
- Machine learning generates predictive intelligence.
- Blockchain-inspired verification ensures information reliability.
- REST APIs deliver trustworthy data to downstream applications.

This architecture demonstrates how supply chain principles can be applied beyond physical logistics to digital information management.

---

# Technology Stack

| Component | Technology |
|------------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Database | H2 Database |
| ORM | Spring Data JPA |
| Machine Learning | Apache Commons Math (OLS Regression) |
| Data Source | data.gov.sg Open Data API |
| Integrity Verification | SHA-256 Merkle Tree |
| Build Tool | Maven |

---

# REST API

## Data Collection

```
POST /api/fetch?limit=500
```

Fetches the next batch of HDB resale transactions.

---

## Historical Transactions

```
GET /api/transactions/recent
```

Returns the latest transaction records.

```
GET /api/transactions?town=BEDOK
```

Returns transactions within a specified town.

```
GET /api/transactions/chain
```

Returns historical transactions for a specific property.

---

## Machine Learning

```
POST /api/model/train
```

Trains the regression model.

```
GET /api/model/predict
```

Predicts resale price.

---

## Blockchain Verification

```
POST /api/blockchain/build
```

Builds the Merkle Tree for daily transactions.

```
GET /api/blockchain/verify/{sourceId}
```

Verifies transaction integrity using a Merkle Proof.

---

# Project Structure

```text
src
 ├── controller
 │      REST APIs
 │
 ├── service
 │      Business Logic
 │
 ├── repository
 │      Data Access Layer
 │
 ├── entity
 │      Database Models
 │
 └── resources
        Configuration
```

---

# Future Improvements

Possible future extensions include:

- PostgreSQL deployment
- Docker containerization
- Cloud deployment (AWS / Azure)
- React frontend dashboard
- Random Forest / XGBoost price prediction
- Smart Contract integration
- Interactive analytics dashboard
- GIS-based property visualization

---

# Author

Developed as an individual software engineering project for exploring:

- Government Open Data Analytics
- Machine Learning
- Information Supply Chain
- RESTful API Development
- Blockchain-inspired Data Integrity Verification