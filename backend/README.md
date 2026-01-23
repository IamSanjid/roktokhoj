## 🧩 RoktoKhoj Backend

This module contains the **Spring Boot backend** for **RoktoKhoj**, responsible for handling donor data, search logic, and geospatial queries.

It uses **PostgreSQL with the PostGIS extension** to efficiently store and query location-based data, making it easy to find nearby blood donors based on area and distance.

---

## 🧰 Tech Stack

* **Spring Boot 4**
* **Spring Web** – REST APIs
* **Spring Data JPA**
* **Hibernate Spatial** – PostGIS integration
* **PostgreSQL + PostGIS**
* **Flyway** – Database migrations
* **Bean Validation** – Input validation
* **Lombok** – Boilerplate reduction
* **JWT** - Authentication
* **Maven**
* **Java 25**

---

## 🛠️ Prerequisites

Before running the backend, make sure you have:

* **Java 25**
* **PostgreSQL** installed
* **PostGIS extension enabled** in PostgreSQL
* **Maven** (or use the Maven Wrapper)

---

## 🔐 Environment Configuration

The backend is configured using environment variables.

### Variables

| Variable      | Description                                   | Default         |
|---------------|-----------------------------------------------|-----------------|
| `DB_HOST`     | PostgreSQL host                               | `localhost`     |
| `DB_PORT`     | PostgreSQL port                               | `5432`          |
| `DB_NAME`     | Database name (created if missing)            | `roktokhoj_api` |
| `DB_USERNAME` | Database username                             | `postgres`      |
| `DB_PASSWORD` | Database password                             | ***required**   |
| `DDL_AUTO`    | Hibernate DDL mode                            | `validate`      |
| `JWT_SECRET`  | The secret key which will be used for signing | ***required**   |
| `ORIGINS`     | Allowed origins for CORS                      | `*`             |

### Example `.env`

```shell
DB_HOST=localhost
DB_PORT=5432
DB_NAME=roktokhoj_api
DB_USERNAME=postgres
DB_PASSWORD=1234
DDL_AUTO=validate
JWT_SECRET=roktokhoj_api_super_secret
```

> 💡 `DDL_AUTO` is optional and maps to `spring.jpa.hibernate.ddl-auto`.
> Flyway is used for schema migrations.

---

## ▶️ Running the Backend

To start the Spring Boot application:

```bash
mvn spring-boot:run
# or
./mvnw spring-boot:run
```

The backend will start on the default Spring Boot port (`8080`) unless configured otherwise.

---

## 🗺️ Geospatial Features

* Stores donor locations using **PostGIS geometry types**
* Performs **distance-based searches** using Hibernate Spatial
* Designed to scale for future features like:

    * Live donor availability
    * Real-time location updates
    * Emergency broadcast notifications

---

## 🐳 Docker Support

You can run the RoktoKhoj backend and a PostGIS database together using **Docker** and **Docker Compose**.

Make sure you have **Docker** and **Docker Compose** installed first.

### 📦 Included Files

* `Dockerfile` — Builds the backend image
* `docker-compose.yml` — Starts backend + PostGIS

---

### 🧱 Build & Run (Quick)

```bash
docker compose up --build
```

This builds the backend image and starts both the backend and the PostGIS database.

---

### 🔌 Configurable Ports

The compose file supports **optional environment variables** so you can change host ports without editing the YAML:

| Variable       | Purpose                              | Default |
|----------------|--------------------------------------|---------|
| `DB_PORT`      | Port on your machine for PostgreSQL  | `5432`  |
| `BACKEND_PORT` | Port on your machine for the backend | `8080`  |

In `docker-compose.yml` we use variable interpolation like:

```yaml
ports:
  - "${DB_PORT:-5432}:5432"
```

This means Docker Compose will:

* Use the value of `DB_PORT` from your environment or `.env` file
* Fall back to `5432` if it’s not set

---

### 📁 Optional `.env` File

You can create a `.env` file in the same directory as your `docker-compose.yml` to set defaults:

```env
DB_PORT=5433
BACKEND_PORT=9090
DB_PASSWORD=supersecret
```

Then just call:

```bash
docker compose up --build
```

Docker Compose will automatically pick up the `.env` file and apply the overrides.

---

### 📌 Notes

* The backend connects to the database using service name `db`, so internal networking works regardless of host port choice.
* Keep sensitive info out of your repo — add `.env` to `.gitignore` if needed.
* The `resources/**/config.json` of the RoktoKhoj-UI also needs to be updated with the port and host.
* You can stop everything with:

```bash
docker compose down
```

---

## 🚧 Notes

* Make sure PostGIS is installed with PostgreSQL **before** starting the app
* Database migrations are handled via **Flyway**
* The backend is intended to be used together with the **RoktoKhoj JavaFX UI**
  * The `resources/**/config.json` also needs to be synced with the backend's host and port.