# ebank-service

Manages **bank accounts**. It calls `customer-service` through **OpenFeign** (with a **Resilience4j** circuit breaker)
to get the account owner, and exposes its operations as a **REST API** and as **MCP tools**.

## Domain

`BankAccount` (JPA entity)

| Field | Type | Notes |
|---|---|---|
| `id` | `String` | Random UUID, set on creation |
| `createdAt` | `Date` | Set on creation |
| `type` | `String` | e.g. `CURRENT`, `SAVING` |
| `balance` | `Double` | |
| `customerId` | `Long` | References a customer in `customer-service` |
| `customer` | `Customer` | `@Transient`, filled through Feign |

`Customer` (in `model/`) is a plain DTO (`id`, `nom`, `email`) that mirrors the remote entity.

## REST API

Base path: `/accounts`

| Method | Path | Description |
|---|---|---|
| `GET` | `/accounts` | List all accounts |
| `GET` | `/accounts/{id}` | Get an account with its customer details |
| `POST` | `/accounts` | Create an account |

```bash
curl -X POST http://localhost:8057/accounts \
  -H "Content-Type: application/json" \
  -d '{"type":"CURRENT","balance":5000,"customerId":1}'

curl http://localhost:8057/accounts
```

Swagger UI: http://localhost:8057/swagger-ui.html

## Inter-Service Communication

`CustomerRestClient` is a Feign client resolved through Eureka (`@FeignClient(name = "customer-service")`):

```java
@GetMapping("/customers/{id}")
@CircuitBreaker(name = "customerService", fallbackMethod = "getDefaultCustomer")
Customer getCustomerById(@PathVariable Long id);
```

If `customer-service` is down, the fallback returns a `Customer` that has only its `id` set,
so `GET /accounts/{id}` still responds.

## MCP Tools

MCP server endpoint: `http://localhost:8057/mcp` (protocol: streamable HTTP)

| Tool | Description |
|---|---|
| `getAllBankAccount` | Get all bank accounts |
| `getBankAccountById` | Get a bank account by ID |
| `saveBankAccountById` | Add a new bank account (`balance`, `type`, `customerId`) |

The tools are the `@McpTool` methods of `EBankService`.

## Configuration

| Property | Value |
|---|---|
| `server.port` | `8057` |
| `spring.datasource.url` | `jdbc:h2:mem:ebank-db` |
| `spring.cloud.discovery.enabled` | `true` (registers in Eureka as `EBANK-SERVICE`) |
| `spring.ai.mcp.server.protocol` | `streamable` |

## Run

```bash
./mvnw spring-boot:run
```

Requires `eureka-service`, and `customer-service` for customer lookups.

## Project Layout

```
org.enset.ebankservice
├── EbankServiceApplication      # entry point, @EnableFeignClients
├── controllers/EbankRestController
├── entities/BankAccount
├── feign/CustomerRestClient     # Feign client + circuit breaker
├── model/Customer               # remote customer DTO
├── repositories/BankAccountRepository
└── services/EBankService        # business logic + MCP tools
```

## Dependencies

Spring Web MVC, Spring Data JPA, H2, Eureka client, OpenFeign, Resilience4j circuit breaker, Actuator,
springdoc-openapi, `spring-ai-starter-mcp-server-webmvc`, Lombok.
