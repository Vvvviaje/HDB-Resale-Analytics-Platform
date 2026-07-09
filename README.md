# Government Data Intelligence Platform

### Case Study: Singapore HDB Resale Market Analytics

A Spring Boot backend platform that transforms Singapore Government Open Data into actionable business intelligence through automated data engineering, statistical learning and cryptographic data integrity verification.

---

# Project Overview

Government Data Intelligence Platform is a backend data intelligence platform developed using Java 21, Spring Boot and Spring Data JPA. It demonstrates an end-to-end data intelligence workflow by collecting, processing, analysing and verifying publicly available government data.

Using official HDB resale transaction records from the Singapore Government Open Data API (data.gov.sg), the platform automatically synchronises transaction data into a local H2 database, provides historical transaction analytics, trains an Ordinary Least Squares (OLS) Multiple Linear Regression model for resale price prediction, and generates SHA-256 based Merkle Trees to ensure historical data integrity.

Rather than focusing solely on housing transactions, the project demonstrates how raw government data can be transformed into reliable decision-support intelligence through a complete end-to-end data intelligence pipeline.

The platform integrates multiple data engineering and analytics components:

- Automated government open data acquisition
- Persistent local database management
- Historical transaction analytics
- Statistical learning based price prediction
- Cryptographic data integrity verification
- RESTful APIs supporting downstream analytical applications

From a systems engineering perspective, the project models an **Information Supply Chain**, where data continuously flows through acquisition, storage, analytics, verification and service delivery. Each processing stage adds value to the original dataset, transforming raw information into trustworthy business intelligence for downstream analytical applications.

---

# Design Objectives

The project was designed with four primary objectives:

- Build an automated government open data ingestion pipeline.
- Transform raw transactional data into structured analytical datasets.
- Generate predictive insights using statistical learning.
- Ensure historical data integrity through cryptographic verification.

These objectives mirror the lifecycle of modern data intelligence systems widely adopted in business analytics, digital transformation and information supply chain management.

---

# Key Features

- Automated collection of HDB resale transactions from the Singapore Government Open Data API
- Incremental API synchronization with duplicate prevention
- Local H2 database persistence using Spring Data JPA
- Historical transaction querying and trend analysis
- Statistical learning based resale price prediction
- SHA-256 based Merkle Tree construction for data integrity verification
- Daily scheduled synchronization using Spring Scheduler
- RESTful APIs supporting analytics and verification services
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
                  Business Applications
```

The platform adopts a modular layered architecture consisting of Controller, Service, Repository and Database layers.

Each module has clearly defined responsibilities while communicating through service interfaces, improving maintainability, scalability and future extensibility.

---

# Information Flow

```text
Government Open Data Source
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
Statistical Learning
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

# Statistical Learning Pipeline

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

The prediction model is implemented using Ordinary Least Squares (OLS) Multiple Linear Regression.

Input features include:

- Floor Area
- Storey Midpoint
- Mature Estate Indicator

Target variable:

- Resale Price

Unlike black-box machine learning models, OLS regression produces interpretable coefficients, allowing each feature's contribution to housing prices to be analysed quantitatively.

Model performance is evaluated using the coefficient of determination (R²).

---

# Cryptographic Data Integrity Verification

```text
                    Root Hash
                  /           \
              Hash12         Hash34
             /     \        /      \
          Hash1   Hash2   Hash3   Hash4
            │        │       │        │
           Tx1      Tx2     Tx3      Tx4
```

Each transaction is converted into a SHA-256 hash.

Transactions collected on the same day are grouped into a blockchain-inspired Merkle Tree.

The generated Root Hash is stored for future verification.

Given any transaction, the system reconstructs the corresponding Merkle Proof to verify whether historical data has been modified without requiring the complete dataset.

---

# Information Supply Chain Perspective

Although the application analyses housing transactions, its overall architecture follows the concept of an Information Supply Chain.

```text
Government Open Data
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

- Government Open Data serves as the upstream data source.
- Data acquisition represents inbound information logistics.
- Local database storage functions as information inventory.
- Statistical learning generates value-added analytical intelligence.
- Cryptographic verification ensures information quality and trustworthiness.
- REST APIs deliver reliable analytical results to downstream applications.

This demonstrates how supply chain principles can be applied beyond physical logistics to digital information management systems.

---

# Engineering Highlights

- Incremental API synchronization using pagination
- Duplicate prevention through unique transaction identifiers
- Automated daily synchronization using Spring Scheduler
- Layered architecture following the Controller-Service-Repository pattern
- Predictive analytics using OLS regression
- SHA-256 Merkle Tree construction for transaction integrity verification
- RESTful API design supporting analytical and predictive services
- Modular architecture supporting future cloud deployment and system expansion

---

# Technology Stack

| Component | Technology |
|------------|------------|
| Programming Language | Java 21 |
| Framework | Spring Boot 3.3 |
| ORM | Spring Data JPA |
| Database | H2 Database |
| HTTP Client | RestTemplate |
| JSON Parsing | Jackson |
| Statistical Learning | Apache Commons Math (OLS Regression) |
| Cryptography | SHA-256 |
| Scheduling | Spring Scheduler |
| Build Tool | Maven |

---

# REST APIs

## Data Collection

```http
POST /api/fetch?limit=500
```

Synchronises the next batch of HDB resale transactions.

---

## Historical Analytics

```http
GET /api/transactions/recent
```

Returns the latest transaction records.

```http
GET /api/transactions?town=BEDOK
```

Returns transactions within a specified town.

```http
GET /api/transactions/chain
```

Returns the historical transaction chain of a property.

---

## Statistical Learning

```http
POST /api/model/train
```

Trains the regression model using historical transactions.

```http
GET /api/model/predict
```

Predicts the resale price of a property.

---

## Data Integrity Verification

```http
POST /api/blockchain/build
```

Constructs the daily Merkle Tree.

```http
GET /api/blockchain/verify/{sourceId}
```

Verifies transaction integrity using the corresponding Merkle Proof.

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
       Application configuration
```

---

# Future Enhancements

### Version 2

- PostgreSQL migration
- Docker containerization
- Automated integration testing

### Version 3

- React analytics dashboard
- Interactive GIS property visualization
- Random Forest and XGBoost prediction models

### Version 4

- Cloud deployment on AWS or Azure
- Streaming data pipeline
- Interactive business intelligence dashboard
- Distributed data processing

---

# Technical Competencies Demonstrated

This project demonstrates practical experience in:

- Backend Software Engineering
- RESTful API Development
- Data Engineering
- Government Open Data Integration
- Statistical Learning
- Predictive Analytics
- Information Supply Chain Design
- Cryptographic Data Integrity Verification
- Layered Software Architecture
- Database Design using Spring Data JPA

---

# Author

Developed as an independent software engineering project to explore data engineering, predictive analytics, software architecture and trustworthy data processing using government open data.