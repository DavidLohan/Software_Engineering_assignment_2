[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/dPOK9SZ_)
# musicFinder 🎸

musicFinder is a Java-based web application that allows users to search for songs, fetch their lyrics, and generate related YouTube video search links. The application uses a Spring Boot backend with integrated APIs to fetch song lyrics and generate YouTube search links based on song and artist name inputs.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Running the Project](#running-the-project)
- [Using the Application](#using-the-application)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- Search for songs by artist and song title.
- Fetch song lyrics using the Lyrics.ovh API.
- Generate a YouTube search link for the song.
- Simple front-end interface to interact with the application.

---

## Technologies

- Java 17: Programming language.
- Spring Boot: Backend framework for building the REST API.
- Maven: Dependency and build management.
- HTML/CSS/JavaScript: Simple front-end for interacting with the backend.
- Lyrics.ovh API: API for fetching song lyrics.

---

## Prerequisites

Before you begin, make sure you have the following installed on your machine:

- Java 17 (or later)
- Maven
- Git (to clone the repository)

---

## Setup Instructions

### 1. Clone the Repository

Clone the musicFinder repository to your local machine:
```
git clone https://github.com/eiramlan/musicFinder.git 
cd musicFinder
```

### 2. Build the Project with Maven

Once inside the project directory, use Maven to build the project:
```
mvn clean install
```

This will download the dependencies and create a runnable JAR file in the `target` directory.

---

## Running the Project

### Running Locally with Maven

1. Run the Spring Boot Application:

After building the project, you can run it locally using Maven:
```
mvn spring-boot:run
```

2. Access the Application:

Open your browser and go to:
```
http://localhost:8080/index.html
```

---

## Using the Application

Once the app is running, you can:

1. Enter an artist name and song title in the provided input fields.
2. Click the Search button.
3. The app will display:
   - A YouTube search link for the song.
   - The song lyrics fetched from the Lyrics.ovh API.

---

## Project Structure
```
musicFinder/
├── src/
│   ├── main/
│   │   ├── java/com/example/musicFinder/   # Main Java code
│   │   └── resources/                      # Application resources (HTML, static files)
│   └── test/                               # Unit tests
├── mvnw                                    # Maven wrapper script for Unix/Linux
├── mvnw.cmd                                # Maven wrapper script for Windows
├── pom.xml                                 # Maven configuration file
└── README.md                               # Project documentation (this file)
```
---

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch with a descriptive name.
3. Make your changes.
4. Submit a pull request with a detailed description of the changes you’ve made.

---

## License

This project is licensed under the MIT License. See the LICENSE file for more details.


