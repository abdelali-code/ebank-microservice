# gataway-service

The **API gateway** of the E-Bank system, built on Spring Cloud Gateway (WebFlux).
It is the single entry point for clients and forwards requests to the microservices registered in Eureka.

## Routing

Routing is **dynamic**. A `DiscoveryClientRouteDefinitionLocator` bean in `GatawayServiceApplication` creates one route
per service registered in Eureka, using the service ID as a path prefix and load-balancing with `lb://`:

```
/CUSTOMER-SERVICE/customers  ->  lb://CUSTOMER-SERVICE/customers
/EBANK-SERVICE/accounts      ->  lb://EBANK-SERVICE/accounts
```

`application.yaml` keeps a commented-out **static** route configuration as an alternative:

| Route | Path | Target |
|---|---|---|
| `r1` | `/customers/**` | `http://localhost:8056` |
| `r2` | `/accounts/**` | `http://localhost:8057` |

## Configuration

| Property | Value |
|---|---|
| `server.port` | `9999` |
| `eureka.client.enabled` | `true` |
| `spring.cloud.config.enabled` | `false` |

## Run

```bash
./mvnw spring-boot:run
```

Requires `eureka-service` to be running. Routes show up once the target services have registered.

## Try It

```bash
curl http://localhost:9999/CUSTOMER-SERVICE/customers
curl http://localhost:9999/EBANK-SERVICE/accounts
```

## Dependencies

- `spring-cloud-starter-gateway-server-webflux`
- `spring-cloud-starter-netflix-eureka-client`
- `spring-boot-starter-actuator`
- `lombok`
