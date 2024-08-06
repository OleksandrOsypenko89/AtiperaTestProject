# ATIPERA TEST PROJECT

![GitHub repo size](https://img.shields.io/github/repo-size/OleksandrOsypenko89/AtiperaTestProject)
![GitHub forks](https://img.shields.io/github/forks/OleksandrOsypenko89/AtiperaTestProject)

The program takes the github username and returns all its repositories that are not forks. 
Then for each project in the repository, it lists all its branches and returns their name and sha of the last commit. 
For the program to work, "Accept: application/json" must be present in the request header.

---

### Run the project

#### 1. Clone the project to your computer.
#### 2. There are three ways to start a project to work with it
- Running in IDE
  - Open the project through your IDE
  - Start the Spring Boot application `AtiperaTestProjectApplication` 
- Run via terminal
  - Open terminal
  - Go to the root folder of the project
  - Execute the command ```mvn spring-boot:run```
- Running in docker
  - Open terminal
  - Go to the root folder of the project
  - Execute the command to create a jar file ```mvn install```
  - The next command to create an image ```docker build -t atipera_test_project .```
  - The last command to run the created image ```docker run -p 8080:8080 atipera_test_project``` 
#### 3. Open Postman
  - Select the type of request "GET"
  - In the address bar, type ```localhost:8080/{username}```
  - On the Headers tab, set the following ```Accept: application/json```
#### 4. You can send requests 
  - If there is no header in the request ```Accept: application/json``` then the program won't return anything.
  - If username does not exist in github the program will return a response in the format ```{ “status”: ${responseCode} “message”: ${whyHasItHappened} }```

---

### Stack of technologies used in the project

* Java 21
* Spring Boot 3
* Rest API
* Docker
* Maven
* Slf4j

### Tools used in development

* IntelliJ IDEA
* Postman
* Docker
* GitHub
* Git