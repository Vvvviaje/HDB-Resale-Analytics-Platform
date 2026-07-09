# Singapore HDB Resale Analytics Platform

## Government Open Data Analytics with Machine Learning and Blockchain-inspired Data Integrity Verification

---

## Project Overview

The Singapore HDB Resale Analytics Platform is a Spring Boot based backend application that automatically collects Singapore Housing Development Board (HDB) resale transaction data from the official **data.gov.sg Open Data API**, stores the data locally, performs statistical price prediction using Multiple Linear Regression, and verifies historical data integrity using a SHA-256 Merkle Tree.

Rather than focusing only on data storage, this project demonstrates an end-to-end data pipeline covering data acquisition, persistence, analytics, integrity verification, and decision support.

The project combines concepts from software engineering, data analytics, blockchain-inspired verification, and information supply chain management into a unified system.

---

# Motivation

Public housing transaction data is continuously updated by the Singapore Government.

For analysts and decision makers, simply storing the data is insufficient.

A complete analytics platform should be able to

* continuously synchronize new transactions
* prevent duplicate records
* maintain historical transaction chains
* estimate market prices
* verify historical data integrity
* provide reliable information for future decision making

This project was developed to demonstrate how these functions can be integrated into a modern backend platform.

---

# Key Features

### Automated Government Data Collection

* Retrieve HDB resale transactions directly from data.gov.sg
* Incremental synchronization using offset-based pagination
* Duplicate prevention through unique sourceId validation
* Daily scheduled synchronization using Spring Scheduler

---

### Local Database Persistence

* H2 embedded database
* Spring Data JPA ORM
* Automatic schema generation
* Lightweight deployment without PostgreSQL installation

---

### Historical Transaction Analytics

Users can query

* recent transactions
* transactions by town
* complete historical resale chains for a specific property

This enables trend analysis for individual HDB units across multiple years.

---

### Machine Learning Price Prediction

The platform trains an Ordinary Least Squares Multiple Linear Regression model using historical transaction records.

Independent variables include

* Floor Area
* Storey Midpoint
* Mature Estate Flag

Dependent variable

* Resale Price

The trained model can estimate the resale price of unseen properties through REST APIs.

---

### Blockchain-inspired Data Integrity Verification

Instead of implementing a public blockchain network, this project adopts blockchain-inspired concepts to verify transaction integrity.

For each day's newly collected transactions

* SHA-256 hashes are generated
* Leaf nodes form a Merkle Tree
* Root Hash is permanently stored
* Individual transactions can later be verified through Merkle Proof reconstruction

This provides efficient tamper-evident verification while maintaining low computational overhead.

---

# Supply Chain Perspective

Although the dataset originates from the real estate market rather than logistics, the system follows the architecture of an information supply chain.

```
Government Open Data
        │
        ▼
Data Acquisition
        │
        ▼
Data Storage
        │
        ▼
Data Analytics
        │
        ▼
Integrity Verification
        │
        ▼
Decision Support
```

Each stage transforms raw information into higher-value knowledge.

This mirrors modern digital supply chain systems where data quality, traceability, and reliability are essential for operational decision making.

---

# System Architecture

```
                    data.gov.sg API
                           │
                           ▼
                HdbDataFetchService
                           │
                           ▼
                     H2 Database
               ┌────────┼────────┐
               │        │        │
               ▼        ▼        ▼
      History Query   ML Model  Merkle Tree
               │        │        │
               └────────┼────────┘
                        ▼
               Spring Boot REST API
                        │
                        ▼
                     Frontend
```

---

# Technology Stack

| Layer            | Technology            |
| ---------------- | --------------------- |
| Backend          | Spring Boot 3         |
| Language         | Java 21               |
| Database         | H2 Database           |
| ORM              | Spring Data JPA       |
| Scheduler        | Spring Scheduling     |
| Machine Learning | Apache Commons Math   |
| Blockchain       | SHA-256 + Merkle Tree |
| Build Tool       | Maven                 |
| API              | RESTful API           |

---

# Project Structure

```
src/main/java/com/uraapi
│
├── controller
│   ├── TransactionController
│   ├── PriceModelController
│   └── BlockchainController
│
├── service
│   ├── HdbDataFetchService
│   ├── PriceRegressionService
│   └── MerkleTreeService
│
├── repository
│
├── entity
│
└── UraApiApplication
```

---

# REST APIs

## Data Collection

POST `/api/fetch`

Fetch the next batch of HDB resale transactions.

---

## Transaction Query

GET `/api/transactions`

Query transactions by town.

GET `/api/transactions/recent`

Retrieve recently fetched transactions.

GET `/api/transactions/chain`

Retrieve complete historical transaction records for a property.

---

## Machine Learning

POST `/api/model/train`

Train the regression model using all available historical records.

GET `/api/model/predict`

Predict resale prices using the trained model.

---

## Blockchain Verification

POST `/api/blockchain/build`

Generate a daily Merkle Root.

GET `/api/blockchain/verify/{sourceId}`

Verify transaction integrity through Merkle Proof.

---

# Design Decisions

| Decision                   | Reason                                                        |
| -------------------------- | ------------------------------------------------------------- |
| H2 Database                | Lightweight deployment without external database installation |
| Spring Data JPA            | Simplified persistence layer                                  |
| Multiple Linear Regression | Highly interpretable baseline prediction model                |
| SHA-256                    | Deterministic cryptographic hashing                           |
| Merkle Tree                | Efficient integrity verification with logarithmic proof size  |
| Scheduled Fetch            | Automatic daily synchronization                               |
| Unique sourceId            | Prevent duplicate government records                          |

---

# Challenges

### Duplicate Data

Government APIs may return overlapping records.

**Solution**

Unique sourceId validation before insertion.

---

### Incremental Synchronization

Only newly published records should be downloaded.

**Solution**

Use repository.count() as pagination offset.

---

### Database Keyword Conflict

The column name "month" conflicts with H2 reserved keywords.

**Solution**

Rename the database column to txn_month while preserving the Java field name.

---

### Data Integrity

Historical records should remain verifiable after storage.

**Solution**

Generate daily SHA-256 Merkle Trees and verify through Merkle Proof reconstruction.

---

# Future Improvements

* PostgreSQL deployment
* Docker containerization
* Swagger/OpenAPI documentation
* Random Forest and XGBoost comparison
* Interactive dashboard using React
* Cloud deployment (AWS or Azure)
* Blockchain smart contract integration

---

# License

This project is intended for academic demonstration and educational purposes.

The HDB resale transaction dataset is publicly available through Singapore Government Open Data (data.gov.sg).
