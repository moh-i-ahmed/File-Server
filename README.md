## File Server

**Author**: [Mohammed Ibrahim Ahmed](https://www.linkedin.com/in/moh-i-ahmed/) | [github.com/moh-i-ahmed](https://github.com/moh-i-ahmed)

**Description**: My implementation of a simple RESTful file server built using Java, Spring Boot & Spring Web. The server provides the following endpoints:

| Endpoint  | Description |
| ---------------------------------------------------------------------------------- | ---------------------------------------------------------------------- |
| [http://localhost:8080/files/](http://localhost:8080/files/```)                    | Retrieves list of all uploaded files with their name and download link |
| [http://localhost:8080/files/open_me.txt](http://localhost:8080/files/open_me.txt) | Downloads the open_me.txt file present in the project                  |
| [http://localhost:8080/files/upload](http://localhost:8080/files/```)              | Uploads a file to the server                                           |

### Pre-requisites & setup

This application has been developed and tested **only** with the following, please ensure you have the **Java** and **Gradle 8.5** versions installed.

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle 8.5](https://docs.gradle.org/8.5/)
- [Windows 11, version 22H2](https://learn.microsoft.com/en-us/windows/release-health/status-windows-11-22h2)

### Run instructions

**Running the application:**

1. Open a terminal and navigate to the project's root directory.
   - ```cd <path_to_folder_containing_project\File-Server```

2. Start the file server using the gradle wrapper command:
   - ```.\gradlew bootRun```

3. In your browser or using a program such as Postman, access the endpoints described above.

**Running the unit tests:**

1. Open a terminal and navigate to the project's root directory.
   - ```cd <path_to_folder_containing_project\File-Server```

2. Run the tests using the gradle wrapper command:
   - ```.\gradlew clean test --info```

3. The results will be displayed in the terminal and will also be stored in the folder:
   - ```File-Server\build\test-results folder```

#### Project structure

**File Server:** The file server code is located in the folder ```File-Server\src\main\java\fileServer\FileServer```

**Unit tests:** The unit tests are located in ```File-Server\src\test\java\fileServer\FileServer\FileServerEndpointTests.java```
