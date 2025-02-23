# **Bank Account API - Spring Boot**

A Spring Boot application for managing bank accounts. Features include deposits, withdrawals, account statements, and overdraft limits. Containerized with **Docker** and automated with **GitHub Actions**.

---

## **Features**

- Deposit funds
- Withdraw funds
- Get account statement
- Set/Get overdraft limit

---

## **Technologies**

- **Spring Boot**
- **JDK 21**
- **Docker**
- **GitHub Actions**
- **Swagger/OpenAPI**

---

## **Quick Start**

### **Local Setup**

1. Clone the repo:
   ```bash
   git clone https://github.com/houssam313/bankkata.git
   cd bank-account-api
   ```

2. Build and run:
   ```bash
   mvn clean package
   java -jar target/bank-account-api.jar
   ```

3. Access:
   ```
   http://localhost:8080
   ```

### **Docker Setup**

1. Build and run:
   ```bash
   docker build -t bank-account-api:1.0.0 .
   docker run -p 8080:8080 bank-account-api:1.0.0
   ```

2. Access:
   ```
   http://localhost:8080
   ```

---

## **CI/CD Pipeline**

Automated with GitHub Actions:
- Builds the app
- Runs tests
- Builds and pushes Docker image to Docker Hub

---

## **API Docs**

Access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

---

## **Deploy**

1. Push to Docker Hub:
   ```bash
   docker tag bank-account-api:1.0.0 exalt-user/bank-account-api:1.0.0
   docker push exalt-user/bank-account-api:1.0.0
   ```

2. Run on server:
   ```bash
   docker run -p 8080:8080 exalt-user/bank-account-api:1.0.0
   ```

---

## **License**

MIT License. See [LICENSE](LICENSE) for details.