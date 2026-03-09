# github-access-reporter

GitHub Access Report Service
Overview

This project is a Spring Boot backend service that connects to the GitHub REST API and generates a report showing which users have access to which repositories within a GitHub organization.

The service retrieves all repositories from the organization, fetches contributors for each repository, and generates an aggregated report mapping users → repositories they can access.

The final report is exposed through a REST API endpoint in JSON format.

Features

Fetch repositories from a GitHub organization

Retrieve contributors for each repository

Aggregate users → repositories access data

Parallel API calls using ExecutorService

Pagination support for large organizations

Caching using Caffeine Cache

Basic GitHub API rate-limit awareness

Clean layered architecture
