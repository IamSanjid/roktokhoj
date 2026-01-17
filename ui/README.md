## 🖥️ RoktoKhoj UI

This module contains the **desktop frontend** for **RoktoKhoj**, built using **JavaFX**.
It provides the user interface for browsing, searching, and viewing blood donor information fetched from the backend.

The UI focuses on simplicity and usability, with map-based visualization and form validation to make donor registration and searching smooth.

---

## 🧰 Tech Stack

* **JavaFX 21** – Core UI framework
* **Gluon Maps** – Map rendering and location visualization
* **FormsFX** – Structured and clean form handling
* **ValidatorFX** – Client-side form validation
* **Gson** – JSON parsing for backend communication
* **Maven** – Build and dependency management
* **JDK 25** – Target Java version

---

## 🛠️ Requirements

* **Java 25** (as configured in the compiler plugin)
* **Maven** (or use the included Maven Wrapper)

Make sure `JAVA_HOME` points to the correct JDK version.

---

## ▶️ Running the UI

To run the JavaFX application in development mode:

```bash
mvn javafx:run
# or
./mvnw javafx:run
```

This will launch the desktop application using the configured main class:

```
org.team2.roktokhoj.Launcher
```

---

## 📦 Packaging the Application

To build a **self-contained runtime image** using **jlink**:

```bash
mvn javafx:jlink
# or
./mvnw javafx:jlink
```

The generated application image will be available in the `target/` directory.

This produces a trimmed runtime without:

* Man pages
* Header files
* Debug symbols

Resulting in a smaller and cleaner distribution.

---

## 🗺️ Map & Location Features

* Uses **Gluon Maps** for displaying donor locations
* Designed to work with backend geospatial data (PostGIS)
* Ready for future features like real-time donor availability

---

## 🚧 Notes

* This UI module expects a **running RoktoKhoj backend** for full functionality
* Backend endpoints and base URLs can be configured inside the UI code
* Map and location features may require additional JVM module flags depending on runtime