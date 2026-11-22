# customer-service

Manages the bank's **customers**. It exposes the same operations two ways:
as a **REST API** and as **MCP tools** that the AI chatbot can call.

## Domain

`Customer` (JPA entity)

| Field | Type |
|---|---|
| `id` | `Long` (auto-generated) |
| `nom` | `String` |
| `email` | `String` |

On startup, a `CommandLineRunner` seeds three customers: `mohamed`, `imane` and `anas` (`<name>@gmail.com`).

## REST API

Base path: `/customers`

| Method | Path | Description |
|---|---|---|
| `GET` | `/customers` | List all customers |
| `GET` | `/customers/{id}` | Get a customer by ID |
| `POST` | `/customers` | Create a customer |

```bash
curl http://localhost:8056/customers
curl -X POST http://localhost:8056/customers \
  -H "Content-Type: application/json" \
  -d '{"nom":"sara","email":"sara@gmail.com"}'
```

Swagger UI: http://localhost:8056/swagger-ui.html

## MCP Tools

MCP server endpoint: `http://localhost:8056/mcp` (protocol: streamable HTTP)

| Tool | Description |
|---|---|
| `getAllCustomers` | Get all customers |
| `getCustomerById` | Get a customer by ID |
| `saveCustomer` | Save a new customer |

The tools are the `@McpTool` methods of `CustomerService`.

## Configuration

| Property | Value |
|---|---|
| `server.port` | `8056` |
| `spring.datasource.url` | `jdbc:h2:mem:customer-db` |
| `spring.cloud.discovery.enabled` | `true` (registers in Eureka as `CUSTOMER-SERVICE`) |
| `spring.ai.mcp.server.protocol` | `streamable` |

## Run

```bash
./mvnw spring-boot:run
```

## Project Layout

```
org.enset.customerservice
├── CustomerServiceApplication   # entry point + data seeding
├── controllers/CustomerController
├── entities/Customer
├── repositories/CustomerRepository
└── services/CustomerService     # business logic + MCP tools
```

## Dependencies

Spring Web MVC, Spring Data JPA, H2, Eureka client, Actuator, springdoc-openapi,
`spring-ai-starter-mcp-server-webmvc`, Lombok.
