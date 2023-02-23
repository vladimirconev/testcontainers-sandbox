# Testcontainers-Sandbox
Playground to check up Testcontainers for Java (https://www.testcontainers.org/) for Integration testing. 

# Pre-requisites 
- JDK19+ 
- Maven
- Docker 

# Build 
``mvn clean verify``

# Code formating 
``mvn com.spotify.fmt:fmt-maven-plugin:format``

# Run it 
``docker compose -f .\docker-compose.yml up --build -d``

# Spring Doc Open API 
``http://localhost:8080/swagger-ui/index.html``

Happy Coding! 

