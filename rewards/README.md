Rewards Application

A Spring Boot application for managing authentication, customers, and rewards.
Includes REST APIs for login, token-based authentication, customer creation (with transactions), and rewards calculation.

📂 Project Structure
rewards
├── .mvn/wrapper
│   └── maven-wrapper.properties
├── docs/
├── test-screenshots/
├── auth-api.md
├── customer-api.md
├── src/
│   ├── main/
│   │   ├── java/com/infy/rewards
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CustomerController.java
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── serviceImpl/
│   │   │   └── RewardsApplication.java
│   │   └── resources/
│   │       ├── Table.sql
│   │       └── application.properties
│   └── test/java/com/infy/rewards
│       ├── controller/
│       │   ├── AuthControllerTest.java
│       │   ├── CustomerControllerTest.java
│       ├── serviceImpl/
│       │   └── CustomerServiceImplTest.java
│       └── RewardsApplicationTests.java
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md

⚙️ Requirements

Java 21+

Maven 3.9+

Spring Boot 3.x

MySQL database (configurable in application.properties)

🚀 Running the Application

Clone the repository:

git clone <your-repo-url>
cd rewards


Build & run:

./mvnw spring-boot:run


The application will be available at:

http://localhost:8080

🔑 API Endpoints
Authentication (/customer/authenticate)
Method	Endpoint	Description	Request Body	Response
POST	/customer/authenticate	Authenticate with custName and phoneNo	```json	
{				
"custName": "Yaswanth",				
"phoneNo": "9345678990"				
}				
```	```json			
{				
"token": "jwt-token"				
}				

---

### Customers (`/customers`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST   | `/customers` | Create a new customer with transactions | ```json
{
  "custName": "Alice",
  "phoneNo": "9876543210",
  "transactions": [
    {
      "amount": 120,
      "transactionDate": "2025-08-01"
    },
    {
      "amount": 200,
      "transactionDate": "2025-08-15"
    }
  ]
}
``` | ```json
{
  "custId": 1,
  "custName": "Alice",
  "phoneNo": "9876543210",
  "transactions": [
    {
      "transactionId": 101,
      "amount": 120,
      "transactionDate": "2025-08-01"
    },
    {
      "transactionId": 102,
      "amount": 200,
      "transactionDate": "2025-08-15"
    }
  ]
}
``` |
| GET    | `/customers/{custId}/rewards` | Get total rewards for the last 3 months | - | `Hello {custName}, your total reward points for last 3 months are {points}.` |
| GET    | `/customers/{custId}/rewards/{monthOffset}` | Get rewards for a specific month offset (0=current month, 1=last month, etc.) | - | `Hello {custName}, your reward points for month offset {monthOffset} are {points}.` |

---

## 🧪 Testing

Tests are written using **JUnit 5** and **Mockito**.  
Located under `src/test/java/com/infy/rewards/`

- `AuthControllerTest.java` → Authentication endpoints  
- `CustomerControllerTest.java` → Customer APIs  
- `CustomerServiceImplTest.java` → Service layer tests  
- `RewardsApplicationTests.java` → Application context load  

Run tests with:

```bash
./mvnw test

📸 Screenshots

Screenshots of successful test runs are available under:

test-screenshots/