# E-Bank Microservices App

A distributed e-banking system built with **Spring Boot 4**, **Spring Cloud** and **Spring AI**.
It shows service discovery, an API gateway, inter-service calls with OpenFeign and a circuit breaker,
and an AI chatbot that uses the business services as **MCP (Model Context Protocol) tools**.

## Architecture

```
                         ┌────────────────────┐
                         │   eureka-service   │  :8761
                         │ (service registry) │
                         └─────────▲──────────┘
                    register /     │      \ register
                    discover       │       \
┌──────────┐     ┌─────────────────┴──┐     \
│  Client  │────▶│  gataway-service   │ :9999 \
└──────────┘     │  (Spring Cloud GW) │        \
                 └───────┬─────────┬──┘         \
              lb://      │         │  lb://      \
                         ▼         ▼
          ┌──────────────────┐  ┌──────────────────┐
          │ customer-service │◀─│  ebank-service   │
          │      :8056       │  │      :8057       │
          │  H2 customer-db  │  │  H2 ebank-db     │
          │  MCP server /mcp │  │  MCP server /mcp │
          └────────▲─────────┘  └────────▲─────────┘
                   │   OpenFeign +       │
                   │   Resilience4j      │
                   │                     │
                   │  MCP (streamable HTTP, found via Eureka)
                   └──────────┬──────────┘
                    ┌─────────┴──────────┐
                    │   ebank-chatboot   │  :8081
                    │ Spring AI + OpenAI │
                    │    (MCP client)    │
                    └────────────────────┘
```

## Modules

| Module | Port | Description |
|---|---|---|
| [eureka-service](eureka-service/README.md) | 8761 | Netflix Eureka discovery server |
| [gataway-service](gataway-service/README.md) | 9999 | Spring Cloud Gateway with dynamic routes from Eureka |
| [customer-service](customer-service/README.md) | 8056 | Customer CRUD (REST + MCP tools), H2 database |
| [ebank-service](ebank-service/README.md) | 8057 | Bank accounts (REST + MCP tools), calls customer-service through Feign |
| [ebank-chatboot](ebank-chatboot/README.md) | 8081 | AI chatbot (OpenAI `gpt-4o`) that uses the MCP tools of both services, found through Eureka |

The root `pom.xml` is a plain Spring Boot project (`ebank-ms-app`) and does **not** aggregate the modules.
Each module is a standalone Maven project with its own wrapper.

## Tech Stack

- Java 25
- Spring Boot 4.2.0
- Spring Cloud 2026.0.0-SNAPSHOT (Eureka, Gateway WebFlux, OpenFeign, Resilience4j)
- Spring AI 2.1.0-M1 (OpenAI model, MCP server and MCP client)
- Spring Data JPA + H2 (in-memory)
- Lombok, springdoc-openapi (Swagger UI)

## Prerequisites

- JDK 25
- Maven 3.9+ (or use the `./mvnw` wrapper in each module)
- An OpenAI API key (needed only by `ebank-chatboot`)

## Running the System

Start the services in this order, each in its own terminal:

```bash
# 1. Discovery server
cd eureka-service && ./mvnw spring-boot:run

# 2. Business services
cd customer-service && ./mvnw spring-boot:run
cd ebank-service    && ./mvnw spring-boot:run

# 3. Gateway
cd gataway-service  && ./mvnw spring-boot:run

# 4. Chatbot (needs the OpenAI key)
export OPENAI_API_KEY=sk-...
cd ebank-chatboot   && ./mvnw spring-boot:run
```

## Quick Checks

| What | URL |
|---|---|
| Eureka dashboard | http://localhost:8761 |
| Customers (direct) | http://localhost:8056/customers |
| Accounts (direct) | http://localhost:8057/accounts |
| Customers through the gateway | http://localhost:9999/CUSTOMER-SERVICE/customers |
| Accounts through the gateway | http://localhost:9999/EBANK-SERVICE/accounts |
| Swagger UI (customer / ebank) | http://localhost:8056/swagger-ui.html, http://localhost:8057/swagger-ui.html |
| Chatbot | http://localhost:8081/chat?query=List all customers |

Example: create a bank account for customer `1`:

```bash
curl -X POST http://localhost:8057/accounts \
  -H "Content-Type: application/json" \
  -d '{"type":"CURRENT","balance":5000,"customerId":1}'
```
