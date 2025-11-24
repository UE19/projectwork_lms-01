# 📚 Library Management System (LMS)

<div align="center">

**A powerful, dual-interface Library Management System built with Java**

[Features](#-features) - [Quick Start](#-quick-start) - [Project Structure](#-project-structure) - [Usage](#-usage) - [Contributing](#-contributing)

</div>

---

## 🌟 Overview

Welcome to the **Library Management System**—a comprehensive solution designed to streamline library operations! Whether you're managing a small school library or a large institutional collection, this system provides robust tools for book tracking, student management, and seamless library workflows.

This project offers **two powerful interfaces**:

- **CLI Version**: Lightning-fast command-line interface for power users and administrators
- **GUI Version**: Powerful, intuitive graphical interface with database integration

---

## ✨ Features

### 📖 Core Functionality

- **Book Management**: Add, search, update, and remove books from your collection
- **Student Registration**: Manage student/member information efficiently
- **Issue \& Return**: Streamlined book borrowing and return workflows
- **Search \& Filter**: Quick catalog searches to find books instantly
- **Real-time Availability**: Track book availability and borrowing status

### 🎯 Technical Highlights

- **Dual Interface**: Choose between CLI for speed or GUI for ease of use
- **Database Integration**: PostgreSQL backend for persistent data storage (GUI version)
- **MVC Architecture**: Clean separation of concerns for maintainability
- **Modern Java**: Built with Java 21 and latest language features
- **Cross-Platform**: Runs seamlessly on Windows, Linux, and macOS

---

## 🚀 Quick Start

### Prerequisites

Before you begin, ensure you have:

- **Java JDK 21** or higher installed
- **Git** for cloning the repository
- **PostgreSQL** (for GUI version with database features)
- (Optional) An IDE like IntelliJ IDEA, Eclipse, or VS Code

### Installation

1. **Clone the Repository**

```bash
git clone https://github.com/UE19/projectwork_lms-01.git
cd projectwork_lms-01
```

2. **Choose Your Version**

**For CLI Version:**

```bash
cd "lms (CLI)"
```

**For GUI Version:**

```bash
cd "lms (GUI)"
```

3. **Build the Project**

**On Linux/macOS:**

```bash
./gradlew build
```

**On Windows:**

```bash
gradlew.bat build
```

4. **Run the Application**

```bash
./gradlew run        # Linux/macOS
gradlew.bat run      # Windows
```

---

## 📁 Project Structure

```
projectwork_lms-01/
│
├── lms (CLI)/                          # Command-Line Interface Version
│   ├── app/
│   │   ├── build.gradle                # CLI build configuration
│   │   └── src/
│   │       ├── main/
│   │       │   └── java/
│   │       │       └── lms/
│   │       │           └── cli/
│   │       │               ├── App.java        # Main CLI application
│   │       │               └── model/
│   │       │                   ├── Books.java     # Book entity
│   │       │                   ├── Library.java   # Core library logic
│   │       │                   └── Student.java   # Student entity
│   │       └── test/                   # Unit tests
│   ├── gradle/                         # Gradle wrapper files
│   ├── gradlew                         # Unix Gradle wrapper script
│   ├── gradlew.bat                     # Windows Gradle wrapper script
│   ├── gradle.properties               # Gradle configuration
│   └── settings.gradle                 # Project settings
│
├── lms (GUI)/                          # Graphical User Interface Version
│   ├── app/
│   │   ├── build.gradle                # GUI build configuration
│   │   └── src/
│   │       └── main/
│   │           ├── java/
│   │           │   └── lms/
│   │           │       ├── App.java               # Main JavaFX application
│   │           │       ├── connector/
│   │           │       │   └── db/
│   │           │       │       └── SQLConnector.java  # Database connector
│   │           │       ├── controller/
│   │           │       │   └── LibraryController.java # FXML controller
│   │           │       └── model/
│   │           │           ├── Book.java              # Book entity
│   │           │           ├── LibraryService.java    # Business logic
│   │           │           └── Student.java           # Student entity
│   │           └── resources/
│   │               └── lms/
│   │                   └── library_view.fxml      # JavaFX UI layout
│   ├── gradle/                         # Gradle wrapper files
│   ├── gradlew                         # Unix Gradle wrapper script
│   ├── gradlew.bat                     # Windows Gradle wrapper script
│   ├── gradle.properties               # Gradle configuration
│   └── settings.gradle                 # Project settings
│
└── README.md                           # This file
```

### Architecture Overview

#### CLI Version

- **Simple MVC Pattern**: Direct model-view interaction through console
- **In-Memory Storage**: Fast operations with ArrayList-based data structures
- **Lightweight**: No external dependencies beyond Java standard library

#### GUI Version

- **Full MVC Architecture**:
  - **Model**: Business entities (Book, Student) and service layer (LibraryService)
  - **View**: FXML-based UI (library_view.fxml)
  - **Controller**: Event handling and UI logic (LibraryController)
- **Database Layer**: PostgreSQL integration via SQLConnector
- **JavaFX Framework**: Modern, responsive graphical interface

---

## 🛠️ Dependencies

### CLI Version

| Dependency        | Version | Purpose                      |
| :---------------- | :------ | :--------------------------- |
| **Java**          | 25      | Core language runtime        |
| **Gradle**        | 9.0.0   | Build automation             |

### GUI Version

| Dependency          | Version | Purpose                        |
| :------------------ | :------ | :----------------------------- |
| **Java**            | 25      | Core language runtime          |
| **Gradle**          | 9.0.0   | Build automation               |
| **JavaFX**          | 21      | GUI framework (controls, fxml) |
| **PostgreSQL JDBC** | 42.7.2  | Database connectivity          |
| **OpenJFX Plugin**  | 0.0.14  | JavaFX Gradle plugin           |

### Build Configuration

Both versions use Gradle with:

- **Maven Central** repository for dependency resolution
- **Application plugin** for running Java applications
- **Java toolchain** configured for Java 25

---

## 💡 Usage

### CLI Version

The CLI version provides a text-based menu system for all library operations:

```
═══════════════════════════════════════
   Library Management System (CLI)
═══════════════════════════════════════

1. Add a Book
2. Display All Books
3. Search for a Book
4. Issue a Book
5. Return a Book
6. Add a Student
7. Display All Students
8. Exit

Enter your choice:
```

**Sample Operations:**

- **Add Book**: Enter book details (ISBN, title, author, quantity)
- **Issue Book**: Provide student ID and book ISBN
- **Return Book**: Submit transaction ID to return books
- **Search**: Find books by title, author, or ISBN

### GUI Version

The GUI version offers an elegant, user-friendly interface with:

**Main Features:**

- **Dashboard**: Overview of library statistics
- **Book Management Panel**: Add, edit, delete, and search books
- **Student Management Panel**: Register and manage library members
- **Transaction Panel**: Issue and return books with visual feedback
- **Search Functionality**: Real-time search across all entities

**Database Setup:**

Before running the GUI version, configure your PostgreSQL database:

1. Create a database named `library_db`
2. Update connection details in `SQLConnector.java`:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

3. The application will auto-create necessary tables on first run

**Running the GUI:**

```bash
cd "lms (GUI)"
./gradlew run
```

---


## 🔧 Development

### Building from Source

**Clean build:**

```bash
./gradlew clean build
```

**Build without tests:**

```bash
./gradlew build -x test
```

### IDE Setup

**IntelliJ IDEA:**

1. Open the project folder (`lms (CLI)` or `lms (GUI)`)
2. IntelliJ will auto-detect Gradle and import the project
3. Wait for dependency resolution
4. Run using the green play button or `Shift + F10`

**Eclipse:**

1. File → Import → Gradle → Existing Gradle Project
2. Select the project folder
3. Finish and wait for build
4. Run as Java Application

**VS Code:**

1. Install "Java Extension Pack" and "Gradle for Java"
2. Open project folder
3. Use Command Palette (`Ctrl+Shift+P`) → "Gradle: Run Task"

---

## 🎨 Customization

### Extending Features

**Add new entities:**

1. Create model classes in the `model/` package
2. Add corresponding service methods
3. Update controllers (GUI) or App menu (CLI)

**Database Schema:**

- Modify `SQLConnector.java` for new tables
- Update SQL queries in `LibraryService.java`

**UI Customization (GUI):**

- Edit `library_view.fxml` in Scene Builder or any text editor
- Apply CSS styling by adding stylesheet references
- Customize colors, fonts, and layouts

---

## 🤝 Contributing

Contributions make this project better! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Contribution Ideas

- 📊 Add reporting and analytics features
- 🔐 Implement user authentication and roles
- 📧 Email notifications for due dates
- 🌐 RESTful API backend
- 📱 Mobile app integration
- 🎨 Enhanced UI themes and dark mode

---

## 📝 License

This project is currently **unlicensed**. Please contact the repository maintainers for usage permissions and licensing information.

---

## 👥 Authors

**UE19**
[GitHub Repository](https://github.com/UE19/projectwork_lms-01)

---

## 📬 Support

Encountering issues? Have questions?

- 🐛 [Report bugs](https://github.com/UE19/projectwork_lms-01/issues)
- 💡 [Request features](https://github.com/UE19/projectwork_lms-01/issues)
- 📖 [View documentation](https://github.com/UE19/projectwork_lms-01/wiki)

---

## 🙏 Acknowledgments

- Built with ❤️ using Java and JavaFX
- Gradle for seamless build management
- PostgreSQL for reliable data persistence
- Open-source community for inspiration and support

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

Made with ☕ and 💻

[Back to Top](#-library-management-system-lms)

</div>
<span style="display:none">[^1]</span>

<div align="center">⁂</div>

[^1]: https://github.com/UE19/projectwork_lms-01
