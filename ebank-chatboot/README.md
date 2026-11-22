# ebank-chatboot

An **AI banking assistant** built with Spring AI and OpenAI (`gpt-4o`).
It acts as an **MCP client** and connects to the MCP servers of `customer-service` and `ebank-service`,
so the model can call their tools to answer questions about customers and bank accounts in plain language.

## How It Works

```
User ──GET /chat?query=...──▶ EbankChatbotController
                                   │
                                   ▼
                              EbankAiAgent (ChatClient)
                                ├── MessageChatMemoryAdvisor   (keeps the conversation history)
                                └── ToolCallbackProvider       (MCP tools)
                                       ├── customer  -> http://customer-service/mcp
                                       └── ebank     -> http://ebank-service/mcp
                                                          │ McpDiscoveryConfig: Eureka lookup
                                                          ▼
                                              real host:port of the instance
                                   │
                                   ▼
                               OpenAI gpt-4o
```

The model decides which tools to call, for example `getAllCustomers`, `getBankAccountById` or `saveBankAccountById`.
The agent sends the results back to the model, which writes the final answer.

## API

| Method | Path | Params | Description |
|---|---|---|---|
| `GET` | `/chat` | `query` (default: `Bonjour`) | Send a message to the assistant |

```bash
curl "http://localhost:8081/chat?query=List%20all%20customers"
curl "http://localhost:8081/chat?query=Create%20a%20saving%20account%20with%2010000%20for%20customer%201"
curl "http://localhost:8081/chat?query=Show%20me%20all%20bank%20accounts"
```

## Configuration

| Property | Value |
|---|---|
| `server.port` | `8081` |
| `spring.ai.openai.api-key` | `${OPENAI_API_KEY}` |
| `spring.ai.openai.chat.model` | `gpt-4o` |
| `spring.ai.mcp.client.streamable-http.connections.customer.url` | `http://customer-service` |
| `spring.ai.mcp.client.streamable-http.connections.ebank.url` | `http://ebank-service` |
| `spring.cloud.discovery.enabled` | `true` (registers in Eureka and reads the registry) |

## Service Discovery for MCP

The MCP server URLs are not hardcoded. Each connection uses the **Eureka service name** as its host.
`config/McpDiscoveryConfig` registers an `McpSyncHttpClientRequestCustomizer` that runs before every MCP request.
It asks Spring Cloud LoadBalancer (`LoadBalancerClient.choose`) for a registered instance and rewrites the URI to that
instance's host and port. This is the same discovery mechanism `ebank-service` uses for its Feign client.

> **Scaling note:** the servers use `spring.ai.mcp.server.protocol=streamable`, which keeps a session on the
> instance that opened it. With more than one instance of a service, set `protocol=stateless` on the servers
> so that any instance can answer any request.

## Run

```bash
export OPENAI_API_KEY=sk-...
./mvnw spring-boot:run
```

Start `eureka-service`, `customer-service` and `ebank-service` first, and wait until both services appear in Eureka. The MCP client connects on startup and fails if it can't find them.

> Do not commit your API key. Always pass it through the `OPENAI_API_KEY` environment variable.

## Project Layout

```
org.enset.ebankchatboot
├── EbankChatbootApplication
├── config/McpDiscoveryConfig           # resolves MCP URLs through Eureka
├── agents/EbankAiAgent                 # ChatClient + memory + MCP tools
└── controllers/EbankChatbotController  # GET /chat
```

## Dependencies

Spring Web MVC, `spring-ai-starter-model-openai`, `spring-ai-starter-mcp-client`, Actuator,
springdoc-openapi, Lombok.
