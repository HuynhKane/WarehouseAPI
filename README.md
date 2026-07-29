# Warehouse API

Backend service for a warehouse management system. It provides REST APIs for
inventory, import and export workflows, users, suppliers, customers, storage
locations, reports, notifications, and financial statistics. The service also
includes JWT authentication, STOMP WebSocket messaging, OpenAPI documentation,
and an OpenAI-powered RAG assistant.

## Features

- Product, genre, supplier, customer, user, and storage-location management
- Import-package and export-package approval workflows
- Stock validation and inventory tracking
- Revenue, cost, finance, and import/export statistics
- Reports and notifications
- BCrypt password hashing and JWT generation/validation
- Real-time messaging over STOMP WebSockets
- Product indexing and natural-language questions through a RAG pipeline
- Interactive API documentation with Swagger UI

## Tech stack

- Java 17
- Spring Boot 3.3
- Spring Data MongoDB
- Spring Security and JJWT
- Spring WebSocket/STOMP
- LangChain4j and OpenAI
- Springdoc OpenAPI
- Gradle
- Docker

## Prerequisites

- JDK 17
- A running MongoDB instance or MongoDB Atlas connection
- An OpenAI API key if the RAG endpoints will be used

The Gradle wrapper is included, so a separate Gradle installation is not
required.

## Configuration

Application settings live in
`src/main/resources/application.properties`. Configure at least:

```properties
spring.application.name=WarehouseAPI
server.port=8081
spring.data.mongodb.uri=mongodb://localhost:27017/warehouse
openai.api.key=your-openai-api-key
openai.api-key=your-openai-api-key
```

Both OpenAI property spellings are currently used by the codebase. Keep secrets
out of version control; for local development, environment variables can
override Spring properties:

```bash
export SPRING_DATA_MONGODB_URI='mongodb://localhost:27017/warehouse'
export OPENAI_API_KEY='your-openai-api-key'
```

Spring maps `OPENAI_API_KEY` to the OpenAI property used by the application.

## Run locally

On macOS or Linux:

```bash
chmod +x gradlew # needed once if the executable bit was not preserved
./gradlew bootRun
```

On Windows:

```powershell
.\gradlew.bat bootRun
```

The service starts at `http://localhost:8081`.

## API documentation

After starting the application:

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Main endpoints

| Area | Base path | Description |
| --- | --- | --- |
| Authentication | `/auth` | Register, log in, and log out |
| Users | `/user` | Manage application users |
| Products | `/product` | Manage, search, filter, and sort products |
| Genres | `/genre` | Manage product categories |
| Suppliers | `/supplier` | Manage and search suppliers |
| Customers | `/customer` | Manage and search customers |
| Storage locations | `/storage-location` | Manage and query warehouse locations |
| Imports | `/import-packages` | Create and process import packages |
| Exports | `/export-packages` | Create, approve, and process export packages |
| Statistics | `/statistic` | Inventory, revenue, cost, and finance analytics |
| Reports | `/report` | Manage reports |
| Notifications | `/notification` | Manage notifications |
| RAG assistant | `/api/rag` | Index products and ask questions |

The complete request schemas, parameters, and responses are available in
Swagger UI.

### Authentication example

Register a user:

```bash
curl -X POST http://localhost:8081/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "admin",
    "passwordHash": "change-me",
    "information": {
      "role": "ADMIN"
    }
  }'
```

Log in:

```bash
curl -X POST \
  'http://localhost:8081/auth/login?username=admin&password=change-me'
```

A successful login returns the JWT, user ID, and role. Send the token to
protected endpoints as:

```http
Authorization: Bearer <token>
```

> Note: the current security configuration permits all HTTP routes. The JWT
> filter is installed, but route authorization must be tightened before relying
> on it in production.

### RAG example

Index the current product data:

```bash
curl -X POST http://localhost:8081/api/rag/index
```

Ask a question:

```bash
curl -X POST http://localhost:8081/api/rag/ask \
  -H 'Content-Type: application/json' \
  -d '{"message":"Which products are low in stock?"}'
```

## WebSocket messaging

The STOMP endpoint is:

```text
ws://localhost:8081/warehouse
```

Application destinations use the `/app` prefix and the simple broker publishes
to `/topic`. The included greeting example sends to `/app/hello` and broadcasts
responses on `/topic/greetings`.

## Tests

Run the test suite with:

```bash
./gradlew test
```

## Build

Create an executable JAR:

```bash
./gradlew clean bootJar
java -jar build/libs/WarehouseAPI-0.0.1-SNAPSHOT.jar
```

## Docker

Build the JAR first, then build and run the image:

```bash
./gradlew clean bootJar
docker build -t warehouse-api .
docker run --rm -p 8081:8081 \
  -e SPRING_DATA_MONGODB_URI='mongodb://host.docker.internal:27017/warehouse' \
  -e OPENAI_API_KEY='your-openai-api-key' \
  warehouse-api
```

When MongoDB runs outside Docker on Linux, replace
`host.docker.internal` with an address reachable from the container.

## Project structure

```text
src/
├── main/
│   ├── java/com/WarehouseAPI/WarehouseAPI/
│   │   ├── controller/    REST controllers
│   │   ├── dto/           API request and response objects
│   │   ├── embeddings/    OpenAI embedding integration
│   │   ├── exception/     Global error handling
│   │   ├── langchain/     RAG and LangChain4j integration
│   │   ├── model/         MongoDB documents
│   │   ├── repository/    Spring Data repositories
│   │   ├── security/      JWT and Spring Security configuration
│   │   ├── service/       Business logic
│   │   └── socket/        WebSocket configuration and handlers
│   └── resources/         Application configuration and static assets
└── test/                  Automated tests
```

## Production notes

Before deploying, move database credentials, OpenAI credentials, and the JWT
signing key to environment-backed configuration. Restrict the Spring Security
route matchers, configure allowed WebSocket origins, and avoid enabling verbose
framework logging unless it is needed for diagnostics.
