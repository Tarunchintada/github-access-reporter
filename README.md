![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Maven](https://img.shields.io/badge/Build-Maven-blue)
![Status](https://img.shields.io/badge/Project-Assignment-success)

# GitHub Access Report Service

A backend service built with **Spring Boot** that generates an access report showing which users have access to which repositories within a GitHub organization.

Key highlights:

* Efficiently processes **100+ repositories and 1000+ users**
* Uses **parallel API calls for faster data retrieval**
* Implements **pagination and caching for scalable API usage**

---

# Overview

This project connects to the **GitHub REST API** and generates a report showing which users have access to repositories within a GitHub organization.

The service:

1. Retrieves all repositories from the organization
2. Fetches contributors for each repository
3. Aggregates the data into a **user → repositories mapping**
4. Exposes the final report through a **REST API endpoint**

The response is returned in structured **JSON format**.

---

# Features

* Fetch repositories from a GitHub organization
* Retrieve contributors for each repository
* Aggregate **users → repositories** access mapping
* Parallel API calls using **ExecutorService**
* Pagination support for organizations with many repositories
* Caching using **Caffeine Cache**
* Basic GitHub API rate limit awareness
* Clean layered architecture

---

# Tech Stack

* **Java 17**
* **Spring Boot**
* **RestTemplate** (GitHub API integration)
* **Caffeine Cache**
* **ExecutorService** (parallel processing)
* **Maven**

---

# Project Structure

```
github-access-reporter
│
├── src/main/java/com/github/accessreport
│   ├── client        # GitHub API integration
│   ├── config        # Cache and thread pool configuration
│   ├── controller    # REST endpoints
│   ├── exception     # Global exception handler
│   ├── model         # DTO classes
│   └── service       # Business logic
│
├── src/main/resources
│   └── application.yml
│
├── pom.xml
└── README.md
```

---

# How to Run the Project

## 1. Clone the Repository

```
git clone https://github.com/Tarunchintada/github-access-reporter.git
cd github-access-reporter
```

---

## 2. Configure GitHub Token

The application requires a **GitHub Personal Access Token** to authenticate with the GitHub API.

Generate a token from:

```
GitHub → Settings → Developer Settings → Personal Access Tokens
```

Then set the token as an environment variable.

Mac / Linux:

```
export GITHUB_TOKEN=your_token_here
```

---

## 3. Configure application.yml

Example configuration:

```yaml
spring:
  application:
    name: github-access-reporter

server:
  port: 8080

github:
  token: ${GITHUB_TOKEN}
  base-url: https://api.github.com
  org: your-github-org
  per-page: 100
```

Replace:

```
org: your-github-org
```

with the GitHub organization you want to analyze.

Example:

```
org: netflix
```

---

## 4. Run the Application

```
mvn clean install
mvn spring-boot:run
```

The application will start at:

```
http://localhost:8080
```

---

# API Endpoint

## Generate Access Report

```
GET /api/access-report
```

Example request:

```
http://localhost:8080/api/access-report
```

---

# Example Response

```json
{
  "status": "success",
  "timestamp": "2026-03-09T12:00:00Z",
  "data": {
    "organization": "example-org",
    "totalRepositories": 3,
    "totalUsers": 11,
    "users": [
      {
        "username": "user1",
        "repositories": [
          "repo1",
          "repo2"
        ]
      }
    ]
  }
}
```

---

# Design Decisions

## Pagination

GitHub API returns repository results in pages. Pagination is implemented using `per_page` and `page` parameters to retrieve all repositories beyond the default limit.

---

## Parallel Processing

Fetching contributors for repositories sequentially can be slow. To improve performance, the service uses an **ExecutorService thread pool** to process repository contributor requests in parallel.

---

## Caching

The generated access report is cached using **Caffeine Cache** to reduce repeated GitHub API calls and improve response time.

Cache expiration is configured to **5 minutes**.

---

## Rate Limit Awareness

GitHub API rate limit headers are monitored to ensure the application does not exceed allowed request limits.

---

# Assumptions

* The GitHub token has access to the repositories within the organization
* GitHub contributors endpoint is used to approximate repository access
* Cache expiration is configured for 5 minutes

---

# Author

Tarun
