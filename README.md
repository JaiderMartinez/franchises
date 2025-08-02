# Infrastructure as Code and Application Deployment - Franchise Demo

This project includes an AWS CloudFormation template to deploy a **RDS PostgreSQL** (Free Tier) database and a guide to run a Java application packaged as a Docker container.

---
## 0. Local Setup with Docker Compose

For local development and testing, you can run both the PostgreSQL database and the API using **Docker Compose**.

### **Step 1: Build the JAR**

From the project root, run:

```bash
./gradlew jar
```

This will generate the executable JAR file in the build/libs/ directory.

### **Step 2: Start services with Docker Compose**
Run
```bash
docker-compose -f deployment/docker-compose.yml up --build
```
Both services will be on the same Docker network (`franchise_network`), allowing the API to access the database via the hostname `postgres_db`.

---

## 1. Deploying RDS PostgreSQL via CloudFormation

The template is located at: `deployment/docs/rds-postgresql.yaml`

### **Main Parameters**

- **MyPCIP**:\
  Defines the IP (or range) from which PostgreSQL database access is allowed.
    - Example: `201.184.1.10/32`
    - Default value: `201.184.1.10/32/32`\
      Change this value if you need access from a different IP.

### **Deployment Steps**

1. Log in to the [AWS Console](https://console.aws.amazon.com/).

2. Go to **CloudFormation** > **Create stack** > **With new resources (standard)**.

3. Upload the `rds-postgresql.yaml` file.

4. Fill in the **MyPCIP** parameter with the public IP address from which you will connect (or `0.0.0.0/0` for open access, **not recommended**).

5. Optionally, customize the following values or use the defaults:

    - **DBInstanceIdentifier**: franchises-postgres-freetier
    - **MasterUsername**: postgres
    - **MasterUserPassword**: ChangeMe123!

6. Wait until the stack creation completes.\
   The RDS endpoint will be shown in the **outputs** of the stack.

#### **⚠️ Security Notice**

> - By default, the template allows global access (`0.0.0.0/0`).\
    >   **This is insecure.**\
    >   For better security, set the `MyPCIP` parameter to your actual IP or an exact access range.
> - Do not use the default password in production.
> - The `PubliclyAccessible` parameter is enabled to allow connections from outside AWS. Disable it if you do not need external access.

---

## 2. Building and Running the Docker Container

### **Build the Image**

```bash
docker build -f deployment/Dockerfile -t franchises-app .
```

- Ensure your application's JAR file and Dockerfile are in the correct paths.

### **Prepare the Environment File**

Edit or create the file:\
`deployment/docs/config.env`\
Sample variables:

```
R2DBC_URL=
R2DBC_USERNAME=
R2DBC_PASSWORD=
```

### **Run the Container**

```bash
docker run -d --name franchises-app -p 8080:8080 --env-file deployment/docs/config.env franchises-app
```

- Access the application at: [http://localhost:8080](http://localhost:8080)
- Change the ports if your app uses a different port.

---

## 3. Recommendations and Warnings

- **Do not expose the database to the entire world (**``**) unless for disposable testing purposes.**
- Delete the database when not in use to avoid unexpected charges.
- Do not upload sensitive files (such as `.env` or passwords) to public repositories.
- Change the default password (`ChangeMe123!`) before using it in any real environment.

---

## 4. API Testing with Postman and Swagger

### **Using Postman**

- A Postman collection is included to simplify API testing and exploration. `deployment/docs/Franchise Management API.postman_collection.json`
- Import the collection into Postman to run sample requests against your deployed API endpoints.
- Make sure to update the environment variables (such as base URL or authentication) to match your deployment.

### **API Documentation (OpenAPI/Swagger)**

- The file `applications/app-service/src/main/resources/swagger.yaml` provides the complete OpenAPI specification for the Franchise API.
- You can visualize and interact with the API using the [Swagger Editor](https://editor.swagger.io/) or other compatible tools.
- To use it:
    1. Open [Swagger Editor](https://editor.swagger.io/)
    2. Import or copy-paste the contents of `swagger.yaml`
    3. Review endpoints, payloads, and try out requests directly from the browser

---
