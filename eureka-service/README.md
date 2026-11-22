# eureka-service

The **service registry** of the E-Bank system, built on Spring Cloud Netflix Eureka Server.
Every other microservice registers here, and the gateway and Feign clients use it to find service instances by name.

## Configuration

| Property | Value |
|---|---|
| `server.port` | `8761` |
| `eureka.client.register-with-eureka` | `false` (the server does not register itself) |
| `eureka.client.fetch-registry` | `true` |

The server is enabled with `@EnableEurekaServer` on `EurekaServiceApplication`.

## Run

```bash
./mvnw spring-boot:run
```

Start this service **first**, before the others.

## Dashboard

Open http://localhost:8761 to see the registered instances (`CUSTOMER-SERVICE`, `EBANK-SERVICE`, `GATAWAY-SERVICE`).

## Dependencies

- `spring-cloud-starter-netflix-eureka-server`
- `spring-boot-restclient`
