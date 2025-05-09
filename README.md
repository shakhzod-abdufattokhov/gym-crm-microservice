# 🏋️‍♂️ Gym CRM Microservice

A scalable and modular Gym CRM system built with **Java 17**, using **Spring Boot microservices architecture**.  
This backend solution manages trainees, trainers, trainings, user roles, and authentication through independently deployable services.  
The system is containerized with **Docker** and orchestrated using **Docker Compose** for seamless development and deployment.

---

## 📌 Project Overview

| Component           | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| `api-gateway`       | Routes incoming requests to the appropriate services using Spring Cloud Gateway |
| `auth-service`      | Handles user authentication, registration, and JWT-based authorization     |
| `user-service`      | Manages trainee/trainer/admin user profiles and roles                       |
| `training-service`  | Manages training sessions, types, and trainer schedules                     |
| `config-server`     | Provides centralized configuration to all microservices                    |
| `discovery-server`  | Service registry using Eureka for dynamic service discovery                 |

---

## 🧱 Architecture

- **Microservice-based design**: Each core function is split into an independent service
- **Spring Cloud Ecosystem**:
  - Spring Cloud Gateway (routing)
  - Eureka (service registry)
  - Config Server (central config)
- **Secure**: Role-based access with **JWT**
- **Database**: Each service uses **PostgreSQL** for data storage
- **Dockerized**: Fully containerized microservices using **Docker**
- **Orchestration**: Easily boot the entire system with **Docker Compose**

---

## 🚀 Getting Started

### 🔧 Prerequisites

- Java 17+
- Docker & Docker Compose
- Git

### ▶️ Run with Docker Compose

```bash
git clone https://github.com/shakhzod-abdufattokhov/gym-crm-microservice.git
cd gym-crm-microservice
docker-compose up --build

