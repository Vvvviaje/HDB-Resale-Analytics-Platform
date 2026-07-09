# Singapore HDB Resale Analytics Platform

### Government Open Data Pipeline, Machine Learning Analytics and Blockchain-inspired Data Integrity Verification

A Spring Boot based backend platform for collecting, analysing and verifying Singapore HDB resale transactions using government open data, statistical learning and blockchain-inspired integrity verification.

---

# Project Overview

The Singapore HDB Resale Analytics Platform is a backend application developed using Java and Spring Boot.

The system automatically retrieves HDB resale transaction data from the official Singapore Government Open Data API (data.gov.sg), stores the records in a local H2 database, provides historical transaction analytics, predicts resale prices using Multiple Linear Regression, and verifies transaction integrity through a SHA-256 based Merkle Tree.

Rather than directly analysing raw government data, the project demonstrates how external data can be transformed into actionable business intelligence through a complete information processing pipeline.

The overall architecture integrates:

- Automated data acquisition
- Persistent data storage
- Historical analytics
- Machine learning prediction
- Blockchain-inspired integrity verification
- RESTful service delivery

Although the project focuses on real estate analytics, its architecture closely resembles an **Information Supply Chain**, where information continuously flows through collection, processing, verification and decision support.

---

# Key Features

- Automated collection of HDB resale transactions from the Singapore Government Open Data API
- Incremental data synchronization with duplicate prevention
- Local H2 database persistence using Spring Data JPA
- Historical transaction querying and property price trend analysis
- Multiple Linear Regression model for resale price prediction
- SHA-256 based Merkle Tree construction for data integrity verification
- Daily scheduled synchronization using Spring Scheduler
- RESTful APIs supporting analytics and verification
- Layered Spring Boot architecture following software engineering best practices

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

The system adopts a modular layered architecture consisting of Controller, Service, Repository and Database layers.

Each module has a clearly defined responsibility while communicating through service interfaces, improving maintainability, scalability and future extensibility.

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

This processing pipeline resembles an **Information Supply Chain**, where information continuously flows through acquisition, transformation, verification and analytical decision support.

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

The prediction model is built using Ordinary Least Squares (OLS) Multiple Linear Regression.

Input features include:

- Floor Area
- Storey Midpoint
- Mature Estate Indicator

Target variable:

- Resale Price

Unlike black-box machine learning models, OLS regression provides interpretable coefficients, allowing each feature's contribution to housing prices to be analysed.

Model performance is evaluated using the coefficient of determination (R²).

---

# Blockchain-inspired Data Integrity Verification

```text
                    Root Hash
                  /           \
              Hash12         Hash34
             /     \        /      \
          Hash1   Hash2   Hash3   Hash4
            │        │       │        │
           Tx1      Tx2     Tx3      Tx4
```

Every transaction is transformed into a SHA-256 hash.

Transactions collected on the same day are grouped into a Merkle Tree.

The generated Root Hash is stored for future verification.

Given any transaction, the system reconstructs the corresponding Merkle Proof and verifies whether historical data has been modified without requiring the complete dataset.

---

# Information Supply Chain Perspective

Although the application analyses housing transactions, its overall architecture follows the concept of an Information Supply Chain.

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

From an information supply chain perspective:

- Government Open Data acts as the upstream information supplier.
- Data acquisition represents inbound information logistics.
- Local database storage functions as information inventory.
- Statistical analysis and machine learning generate value-added processing.
- Merkle Tree verification serves as information quality assurance.
- REST APIs deliver trusted analytical results to downstream consumers.

This demonstrates how supply chain principles can be applied beyond physical logistics to digital information management systems.

---

# Engineering Highlights

- Incremental data synchronization using API pagination
- Duplicate prevention through unique transaction identifiers
- Automated daily synchronization using Spring Scheduler
- Layered architecture following the Controller-Service-Repository pattern
- Statistical price prediction using OLS Multiple Linear Regression
- SHA-256 Merkle Tree construction for transaction integrity verification
- RESTful API design supporting historical analytics and predictive services
- Modular design supporting future cloud deployment and system expansion

---

# Technology Stack

| Component | Technology |
|------------|------------|
| Programming Language | Java 21 |
| Framework | Spring Boot 3.3 |
| Database | H2 Database |
| ORM | Spring Data JPA |
| Machine Learning | Apache Commons Math (OLS Regression) |
| Data Source | data.gov.sg Open Data API |
| Data Integrity | SHA-256 Merkle Tree |
| Scheduling | Spring Scheduler |
| Build Tool | Maven |

---

# REST APIs

## Data Collection

```
POST /api/fetch?limit=500
```

Synchronises the next batch of HDB resale transactions.

---

## Historical Analytics

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

Returns the historical transaction chain of a property.

---

## Machine Learning

```
POST /api/model/train
```

Trains the regression model using historical transactions.

```
GET /api/model/predict
```

Predicts the resale price of a property.

---

## Data Integrity Verification

```
POST /api/blockchain/build
```

Constructs the daily Merkle Tree.

```
GET /api/blockchain/verify/{sourceId}
```

Verifies transaction integrity using the Merkle Proof.

---

# Project Structure

```text
src
├── controller
│      REST API endpoints
│
├── service
│      Business logic
│
├── repository
│      Data persistence layer
│
├── entity
│      Database models
│
└── resources
       Configuration files
```

---

# Future Roadmap

### Version 2

- PostgreSQL migration
- Docker containerization
- Automated integration testing

### Version 3

- React analytics dashboard
- Interactive GIS property visualization
- Random Forest and XGBoost price prediction

### Version 4

- Cloud deployment (AWS / Azure)
- Smart contract integration
- Real-time streaming data pipeline
- Interactive business intelligence dashboard

---

# Learning Outcomes

This project demonstrates practical experience in:

- Backend Software Engineering
- RESTful API Development
- Data Engineering
- Government Open Data Integration
- Machine Learning for Predictive Analytics
- Information Supply Chain Design
- Blockchain-inspired Data Integrity Verification
- Layered Software Architecture
- Database Design using Spring Data JPA

---

# Author

Developed as an individual software engineering project for exploring modern backend architecture, machine learning applications, government open data analytics, and blockchain-inspired data integrity verification.