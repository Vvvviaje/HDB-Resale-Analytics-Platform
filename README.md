# Government Data Intelligence Platform

## A Case Study on Singapore HDB Resale Market Analytics

**Java 21 · Spring Boot 3.3.2 · Spring Data JPA · Apache Commons Math ·
H2 Database**

A Spring Boot--based data intelligence platform that transforms
Singapore Government Open Data into actionable decision-support insights
through automated data acquisition, statistical learning, and
cryptographic data integrity verification.

This project demonstrates an end-to-end information processing workflow
where public datasets are collected, stored, analysed, modelled, and
verified to support reliable decision-making.

------------------------------------------------------------------------

# Project Highlights

  -----------------------------------------------------------------------
  Highlight                           Description
  ----------------------------------- -----------------------------------
  **Data Source**                     Singapore Government Open Data API
                                      (`data.gov.sg`)

  **Data Engineering**                Incremental HDB resale transaction
                                      synchronisation with duplicate
                                      prevention

  **Database**                        H2 Embedded Database with Spring
                                      Data JPA

  **Analytics**                       Historical resale transaction
                                      analysis

  **Prediction**                      Ordinary Least Squares (OLS)
                                      Multiple Linear Regression

  **Integrity**                       SHA-256 Merkle Tree Verification

  **Architecture**                    Layered Spring Boot Architecture
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# System Architecture

``` text
Singapore Government Open Data API
               │
               ▼
     HdbDataFetchService
(Data Acquisition & Processing)
               │
               ▼
     ResaleTransaction Entity
               │
               ▼
   Spring Data JPA Repository
               │
               ▼
         H2 Database
               │
   ┌───────────┼────────────┐
   ▼           ▼            ▼
Analytics  Regression   Merkle Tree
 Service     Service      Service
   │           │            │
   ▼           ▼            ▼
History   OLS Model   Root Hash & Proof
```

------------------------------------------------------------------------

# Project Overview

Built using Java 21, Spring Boot, and Spring Data JPA, this platform
continuously collects official Singapore HDB resale transaction records
from the Government Open Data API and stores them in a local H2
database.

The platform provides:

-   Automated data ingestion and duplicate prevention
-   Historical transaction analytics
-   OLS multiple linear regression model training
-   Resale price prediction
-   SHA-256 Merkle Tree based data integrity verification

Rather than analysing raw datasets directly, the platform transforms
government open data into trustworthy analytical information through an
information processing pipeline.

------------------------------------------------------------------------

# Key Features

## Automated Data Acquisition

-   Incremental synchronisation from the Government Open Data API
-   Duplicate transaction prevention
-   Persistent storage using Spring Data JPA
-   Structured entity mapping

## Historical Analytics

-   Query recent transactions
-   Search transactions by town
-   View complete historical transaction chains for a property

## Statistical Learning

The prediction module trains an Ordinary Least Squares (OLS) Multiple
Linear Regression model using historical transaction records.

Features include:

-   Floor Area
-   Storey Level
-   Mature Estate Indicator

Model performance is evaluated using the coefficient of determination
(R²), while interpretable coefficients quantify the influence of each
feature on resale prices.

## Cryptographic Data Integrity Verification

Each transaction is converted into a SHA-256 hash before being organised
into a Merkle Tree.

The generated Root Hash provides a compact integrity fingerprint,
allowing transaction authenticity to be verified efficiently through
Merkle Proof reconstruction without requiring the complete dataset.

------------------------------------------------------------------------

# Information Processing Flow

``` text
Government Open Data
        │
        ▼
Data Acquisition
        │
        ▼
Local Data Storage
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

------------------------------------------------------------------------

# Technology Stack

  Component              Technology
  ---------------------- ---------------------------
  Programming Language   Java 21
  Backend Framework      Spring Boot 3.3.2
  ORM                    Spring Data JPA
  Database               H2 Database
  Statistical Learning   Apache Commons Math 3.6.1
  Cryptography           SHA-256
  Validation             Spring Boot Validation
  Build Tool             Maven

------------------------------------------------------------------------

# REST APIs

## Transaction Management

  --------------------------------------------------------------------------------
  Method                  Endpoint                         Description
  ----------------------- -------------------------------- -----------------------
  POST                    `/api/fetch?limit=100`           Synchronise the next
                                                           batch of HDB resale
                                                           transactions

  GET                     `/api/transactions/recent`       Retrieve the latest 50
                                                           transactions

  GET                     `/api/transactions?town=BEDOK`   Query transactions by
                                                           town

  GET                     `/api/transactions/chain`        Retrieve historical
                                                           transaction chain for a
                                                           property
  --------------------------------------------------------------------------------

## Statistical Learning

  -----------------------------------------------------------------------
  Method                  Endpoint                Description
  ----------------------- ----------------------- -----------------------
  POST                    `/api/model/train`      Train the OLS
                                                  regression model

  GET                     `/api/model/predict`    Predict resale price
                                                  using the trained model
  -----------------------------------------------------------------------

## Data Integrity Verification

  -------------------------------------------------------------------------------------
  Method                  Endpoint                              Description
  ----------------------- ------------------------------------- -----------------------
  POST                    `/api/blockchain/build`               Build a Merkle Tree and
                                                                generate the daily Root
                                                                Hash

  GET                     `/api/blockchain/verify/{sourceId}`   Verify transaction
                                                                integrity using a
                                                                Merkle Proof
  -------------------------------------------------------------------------------------

------------------------------------------------------------------------

# Project Structure

``` text
src/main/java/com/uraapi
├── controller
│   ├── TransactionController
│   ├── PriceModelController
│   └── BlockchainController
├── service
│   ├── HdbDataFetchService
│   ├── PriceRegressionService
│   └── MerkleTreeService
├── repository
├── entity
└── UraApiApplication
```

------------------------------------------------------------------------

# Future Enhancements

-   PostgreSQL migration
-   Docker containerisation
-   Automated integration testing
-   React analytics dashboard
-   Cloud deployment on AWS or Azure

------------------------------------------------------------------------

# Technical Competencies Demonstrated

-   Backend Software Engineering
-   RESTful API Design
-   Data Engineering
-   Government Open Data Integration
-   Statistical Learning
-   Database Design and Persistence
-   Cryptographic Data Integrity Verification
-   Information Processing Pipeline Design

------------------------------------------------------------------------

# Author

Developed as an independent software engineering project demonstrating
backend engineering, data engineering, predictive analytics, and
trustworthy information processing using Singapore Government Open Data.
