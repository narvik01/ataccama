# Ataccama interview task

### Requirements
* Java 11
* Docker

## Usage
```
./gradlew clean build bootBuildImage && docker-compose -f docker-compose.yml -f docker-compose.db.yml up
```
http://localhost:8080/swagger

## Testing
* Spotbugs static analysis, generates `build/reports/spotbugs/main.xml`
* Spock integration testing
* Testcontainers to run Postgres DB in integration tests

## Technologies
* Spring Boot
* Spring JPA
* Liquibase
* Mapstruct
* Lombok for fast POC implementation

## TODO/issues
* do not send DB passwords in responses
* encrypt passwords in database