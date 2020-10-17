# Ataccama interview task

### Requirements
* Java 11
* Docker

## Usage
Running:
```
./gradlew clean build bootBuildImage && docker-compose -f docker-compose.yml -f docker-compose.dbexamples.yml up
docker-compose down --remove-orphans
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

Example calls:
```
curl -v localhost:8080/connections -d@'data/external1.json' -H'Content-Type: application/json'
```

## Testing
* Spotbugs static analysis, generates <build/reports/spotbugs/main.xml>
* Spock integration testing
* Testcontainers to run Postgres DB in integration tests
* Example data from Sakila port Pagila https://github.com/devrimgunduz/pagila

## Technologies
* Spring Boot
* Spring JPA
* Liquibase
* Mapstruct
* Lombok for fast POC implementation

## TODO/identified issues
* docker image size and performance
* do not send DB passwords in responses
* encrypt passwords in database