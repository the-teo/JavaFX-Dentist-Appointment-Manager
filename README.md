# 🦷 Dentist Appointment Management System (JavaFX)

A desktop application designed for managing patient and appointment records, built with JavaFX and demonstrating multiple persistence layers and clean architecture principles.

## ✨ Features

* **Patient & Appointment CRUD:** Complete creation, reading, updating, and deletion functionality.
* **JavaFX GUI:** A modern, interactive user interface for managing data.
* **Advanced Persistence Layer:** Supports four distinct file-based repository types, allowing data to be saved and loaded from configurable formats:
    * Plain Text (`.txt`)
    * Binary Serialization (`.bin`)
    * JSON Serialization (`.json`)
    * XML Serialization (`.xml`)
* **Java Module System (JPMS):** Configured and built using Java 21+ with explicit module dependencies (`module-info.java`) for JavaFX, Gson, and Jakarta XML Binding (JAXB).

## 🛠️ Technology Stack

* **Language:** Java (JDK 21+)
* **Framework:** JavaFX (UI Toolkit)
* **Persistence Libraries:**
    * Gson (for JSON serialization)
    * Jakarta XML Binding (JAXB) (for XML serialization)
    * SQLite-JDBC (for potential future database integration)

## 🚀 How to Run

1.  **Clone the Repository:**
    ```bash
    git clone [Your Repository URL]
    ```
2.  **Dependencies:** Ensure you have the JavaFX SDK and the required JAXB/Gson JARs attached to your project module (as defined in `module-info.java`).
3.  **Run:** Execute the `main.MainApplication` class.

## 📂 Project Structure Highlights

The project adheres to a clean architectural design:

* `domain/`: Contains the core entities (`Patient`, `Appoitment`).
* `repository/`: Contains all specialized repository implementations (e.g., `PatientXmlRepo`, `AppointmentJsonRepo`).
* `controller/`: Contains the JavaFX controllers linked to the FXML files.
* `gui/`: Contains the FXML layouts and the main application launcher.
