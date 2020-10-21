# Ataccama interview task

Ondřej Hamák, hamak@email.cz

## Requirements
* Java 11
* Docker

## Usage
Build and run:
```
./gradlew clean build bootBuildImage && docker-compose -f docker-compose.yml -f docker-compose.dbexamples.yml up
...
docker-compose down --remove-orphans
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

Example calls:
```
curl -v localhost:8080/connections -d@'data/external1.json' -H'Content-Type: application/json'
curl -v localhost:8080/connections
```

## Testing
* Spotbugs static analysis, generates <build/reports/spotbugs/main.xml>
* Spock integration testing
* Testcontainers to run Postgres DB in integration tests
* Example data from https://www.postgresql.org/ftp/projects/pgFoundry/dbsamples/

## Technologies
* Spring Boot
* Spring JPA
* Liquibase - DB initialization
* Schemacrawler - DB introspection and model
* Mapstruct - POJO mappings
* Lombok for fast POC implementation
* Springfox, Swagger - API documentation

## TODO/identified issues
* docker image size and performance
* do not send DB passwords in responses
* encrypt passwords in database
* possible SQL injection via table name in data preview, should be validated