# ⚙️ Matching Engine Service

Matching Engine Service is the core microservice of the **Stock Trading Simulation Platform**.  
It simulates a real-world stock exchange matching engine by maintaining an in-memory order book, matching BUY and SELL orders, and executing trades using price-time priority.

---

## 🚀 Features

- 📊 In-memory order book (no database)
- ⚡ Real-time order matching (high performance)
- 💰 Price-time priority (FIFO for same price)
- 🔄 Partial order execution support
- 🧠 Separate order books per stock symbol
- 📡 Event-driven architecture using Redis Pub/Sub
- 📝 Trade generation after successful match
- 🔍 REST endpoint for testing (`/match`)
- 🧱 Clean layered architecture
- 📜 Detailed logging for debugging and tracing

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3.x
- Redis (Docker)
- Lombok
- Jakarta Validation

---

## 📂 Project Structure

    com.stock.matching_engine_service
    │
    ├── controller         # REST APIs (testing)
    ├── service            # Business logic
    │   └── impl
    ├── entity             # Trade entity
    ├── dto                # OrderEventDto, TradeResponseDto
    ├── util               # Comparators (Buy/Sell)
    ├── enums              # OrderType
    ├── config             # Redis configuration
    └── MatchingEngineServiceApplication.java

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

    git clone <your-repo-url>
    cd matching-engine-service

---

### 2️⃣ Start Redis (Docker)

    docker run --name stock-redis \
    -p 6379:6379 \
    -d redis

---

### 3️⃣ Configure application.yml

    server:
      port: 8084

    spring:
      application:
        name: matching-engine-service

      redis:
        host: localhost
        port: 6379

    logging:
      level:
        root: INFO
        com.stock.matching_engine_service: DEBUG

---

### 4️⃣ Run the Application

    mvn spring-boot:run

---

## 🔌 API Endpoints (Testing Only)

### 📝 Match Order

    POST /api/v1/matching-engine/match

#### Request Body:

    {
      "orderId": 1,
      "stockSymbol": "INFY",
      "quantity": 10,
      "price": 1500,
      "orderType": "BUY"
    }

---

## 📡 Event-Driven Flow (Redis)

### 🔹 Order Event Channel

    order-events

### 🔹 Trade Event Channel

    trade-events

---

### 🧪 Publish Order via Redis

    PUBLISH order-events "{\"orderId\":1,\"stockSymbol\":\"INFY\",\"quantity\":10,\"price\":1500,\"orderType\":\"BUY\"}"

    PUBLISH order-events "{\"orderId\":2,\"stockSymbol\":\"INFY\",\"quantity\":10,\"price\":1400,\"orderType\":\"SELL\"}"

---

### ✅ Expected Behavior

- Orders are received by Matching Engine
- Orders are added to order book
- Matching logic executes trade
- Trade event is published

---

## 🧠 Matching Logic (Core Concept)

### BUY Orders:
- Sorted by **highest price first**
- FIFO for same price

### SELL Orders:
- Sorted by **lowest price first**
- FIFO for same price

---

### Matching Rule:

    BUY price >= SELL price

---

### Execution Logic:

- Execute minimum of buy & sell quantity
- Reduce quantities accordingly
- Remove fully executed orders
- Generate trade

---

## 🧪 Testing

### 1️⃣ REST API Test

    POST http://localhost:8084/api/v1/matching-engine/match

---

### 2️⃣ Redis Test (Recommended)

    docker exec -it stock-redis redis-cli

    PUBLISH order-events "{\"orderId\":1,\"stockSymbol\":\"INFY\",\"quantity\":10,\"price\":1500,\"orderType\":\"BUY\"}"

---

### 3️⃣ Check Logs

Expected logs:

    Received order from Redis
    Processing order
    Trade executed

---

## 🧠 Key Concepts Implemented

- In-memory matching engine (no DB dependency)
- PriorityQueue for order book
- Comparator-based sorting (price-time priority)
- FIFO using timestamp
- Event-driven architecture using Redis Pub/Sub
- Clean separation of layers
- Logging using SLF4J

---

## 🐳 Docker Notes

- Redis runs on port 6379
- Ensure container is running before starting service

---

## 📌 Future Improvements

- Thread-safe order book (ConcurrentHashMap, locks)
- WebSocket integration for real-time updates
- Persistent trade storage (MySQL)
- Kafka for scalable messaging
- Performance optimization (multi-threaded matching)
- Circuit breaker for fault tolerance

---

## 👨‍💻 Author

Vipul Singh
