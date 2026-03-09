\# GitHub Access Report Service

## Overview

This project is a **Spring Boot backend service** that connects to the GitHub REST API and generates a report showing which users have access to which repositories within a GitHub organization.

The service retrieves all repositories from the organization, fetches contributors for each repository, and generates an aggregated report mapping **users → repositories they can access**.

The final report is exposed through a REST API endpoint in JSON format.

---

## Features

* Fetch repositories from a GitHub organization
* Retrieve contributors for each repository
* Aggregate **users → repositories** access data
* Parallel API calls using `ExecutorService`
* Pagination support for large organizations
* Caching using **Caffeine Cache**
* Basic GitHub API rate-limit awareness
* Clean layered architecture

---

## Tech Stack

* Java 17
* Spring Boot
* RestTemplate (GitHub API integration)
* Caffeine Cache
* ExecutorService (parallel processing)
* Maven

---

## Project Structure

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

## How to Run the Project

### 1. Clone the repository

```
git clone https://github.com/Tarunchintada/github-access-reporter.git
cd github-access-reporter
```

---

### 2. Configure GitHub Token

The application requires a **GitHub Personal Access Token** to authenticate with the GitHub API.

Generate a token from GitHub:

```
GitHub → Settings → Developer Settings → Personal Access Tokens
```

Then set the token as an environment variable.

Mac / Linux:

```
export GITHUB_TOKEN=your_token_here
```

---

### 3. Configure application.yml

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

### 4. Run the application

```
mvn clean install
mvn spring-boot:run
```

The application will start on:

```
http://localhost:8080
```

---

## API Endpoint

### Generate Access Report

```
GET /api/access-report
```

Example request:

```
http://localhost:8080/api/access-report
```

---

## Example Response

```json
{
  "status": "success",
  "timestamp": "2026-03-09T12:00:00Z",
  "data": {
    "organization": "example-org",
    "totalRepositories": 10,
    "totalUsers": 5,
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

## Design Decisions

### Pagination

GitHub API returns repository results in pages.
Pagination is implemented using `per_page` and `page` parameters to fetch all repositories.

### Parallel Processing

Fetching contributors for repositories sequentially can be slow.
To improve performance, an `ExecutorService` thread pool processes repositories in parallel.

### Caching

The generated report is cached using **Caffeine Cache** for a short duration to reduce repeated GitHub API calls and improve response time.

### Rate Limit Awareness

GitHub API rate limit headers are monitored to avoid exceeding API request limits.

---

## Assumptions

* The GitHub token has access to the repositories in the organization.
* Contributors endpoint is used to approximate repository access.
* Cache expiration is configured for 5 minutes.

---

## Future Improvements

* Add retry mechanism for GitHub API failures
* Add Swagger/OpenAPI documentation
* Add unit and integration tests
* Support multiple organizations dynamically

---

## Author

Tarun
