package cz.strangeloop.ataccama

import com.fasterxml.jackson.databind.JsonNode
import cz.strangeloop.ataccama.api.dto.ColumnDto
import cz.strangeloop.ataccama.api.dto.ErrorDto
import cz.strangeloop.ataccama.api.dto.SchemaDto
import cz.strangeloop.ataccama.api.dto.TableDto
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@SpringBootTest(classes = [AtaccamaApplication.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Testcontainers
@Stepwise
@DirtiesContext
@SuppressFBWarnings(value = "SE_NO_SERIALVERSIONID", justification = "serialVersionUID not generated in closures")
class IntrospectionIntegrationTests extends Specification {

    public static final int PORT = 5432
    public static final String DB_EXTERNAL_1 = "external_database_1_1"
    public static final String DB_EXTERNAL_2 = "external_database_2_1"

    @Autowired
    TestRestTemplate rt

    @Shared
    public static final DockerComposeContainer dbContainer =
            new DockerComposeContainer(new File("docker-compose.dbexamples.yml"))
                    .withExposedService(DB_EXTERNAL_1, PORT)
                    .withExposedService(DB_EXTERNAL_2, PORT)

    def setupSpec() {
        dbContainer.start()
    }

    def "connect to nonexisting db"() {
        when: "connected"
            def entity = rt.getForEntity("/connections/00000000-0000-0000-0000-000000000000/schemas", ErrorDto.class)
        then:
            entity.statusCode == HttpStatus.NOT_FOUND
    }

    def "get schemas and tables and columns"() {
        given:
            String location = rt.postForLocation("/connections", [
                    name        : "external1",
                    hostname    : dbContainer.getServiceHost(DB_EXTERNAL_1, PORT),
                    port        : dbContainer.getServicePort(DB_EXTERNAL_1, PORT),
                    databaseName: "externaldb",
                    username    : "external",
                    password    : "extpass"
            ])
            def connectionId = location.tokenize('/')[-1]

        when: "lists of schemas and tables are loaded"
            def schemas = rt.getForObject("/connections/$connectionId/schemas", SchemaDto[].class)
            def tables = rt.getForObject("/connections/$connectionId/schemas/public/tables", TableDto[].class)
            def columns = rt.getForObject("/connections/$connectionId/schemas/public/tables/city/columns", ColumnDto[].class)
        then:
            schemas.length == 3
            schemas.collect { it.name }.contains("public")
            tables.length == 3
            def table = tables.find { it.name == "country" }
            table.primaryKey.contains("code")
            table.foreignKeys.contains("country_capital_fkey")
            table.indexes.collect { it.columns }.flatten().contains("code")
            tables.collect { it.name }.contains("countrylanguage")
            tables.collect { it.name }.contains("city")


            columns.length == 5
            def column = columns.find { it.name == "id" }
            column.columnDataType == "int4"
            column.partOfPrimaryKey == true
            column.partOfUniqueIndex == true

            def col2 = columns.find { it.name == "population" }
            col2.defaultValue == null
            col2.columnDataType == "int4"
            col2.partOfPrimaryKey == false

    }

    def "connection failed, invalid credentials"() {
        given:
            String location = rt.postForLocation("/connections", [
                    name        : "external1",
                    hostname    : dbContainer.getServiceHost(DB_EXTERNAL_1, PORT),
                    port        : dbContainer.getServicePort(DB_EXTERNAL_1, PORT),
                    databaseName: "externaldb",
                    username    : "external",
                    password    : "INVALID_PASSWORD"
            ])
            def connectionId = location.tokenize('/')[-1]

        when: "lists of schemas and tables are loaded"
            def entity = rt.getForEntity("/connections/$connectionId/schemas", ErrorDto.class)
        then:
            entity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            entity.body.message == 'Invalid DB credentials.'
    }

    def "connection failed, network error"() {
        given:
            String location = rt.postForLocation("/connections", [
                    name        : "external1",
                    hostname    : "INVALID_HOSTNAME",
                    port        : dbContainer.getServicePort(DB_EXTERNAL_1, PORT),
                    databaseName: "externaldb",
                    username    : "external",
                    password    : "extpass"
            ])
            def connectionId = location.tokenize('/')[-1]

        when: "lists of schemas and tables are loaded"
            def entity = rt.getForEntity("/connections/$connectionId/schemas", ErrorDto.class)
        then:
            entity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            entity.body.message == "Unknown error."
    }

    def "non public schema"() {
        given:
            String location = rt.postForLocation("/connections", [
                    name        : "external2",
                    hostname    : dbContainer.getServiceHost(DB_EXTERNAL_2, PORT),
                    port        : dbContainer.getServicePort(DB_EXTERNAL_2, PORT),
                    databaseName: "externaldb2",
                    username    : "external2",
                    password    : "extpass2"
            ])
            def connectionId = location.tokenize('/')[-1]

        when: "lists of schemas and tables are loaded"
            def schemas = rt.getForObject("/connections/$connectionId/schemas", SchemaDto[].class)
            def tables = rt.getForObject("/connections/$connectionId/schemas/isoschema/tables", TableDto[].class)
            def columns = rt.getForObject("/connections/$connectionId/schemas/isoschema/tables/country/columns", ColumnDto[].class)
        then:
            schemas.collect { it.name }.contains("isoschema")

            tables.collect { it.name }.contains("country")
            tables.collect { it.name }.contains("subcountry")

            columns.collect { it.name }.contains("two_letter")
            columns.collect { it.name }.contains("country_id")
            columns.collect { it.name }.contains("name")
    }

    def "data preview"() {
        given:
            String location = rt.postForLocation("/connections", [
                    name        : "external1",
                    hostname    : dbContainer.getServiceHost(DB_EXTERNAL_1, PORT),
                    port        : dbContainer.getServicePort(DB_EXTERNAL_1, PORT),
                    databaseName: "externaldb",
                    username    : "external",
                    password    : "extpass"
            ])
            def connectionId = location.tokenize('/')[-1]

        when: "data preview loaded"
            def data = rt.getForObject("/connections/$connectionId/schemas/public/tables/country/data", JsonNode.class)
        then:
            data.size() == 10
            data.get(0).get("continent").textValue() == "Asia"
            data.get(0).get("name").textValue() == "Afghanistan"
            data.get(0).get("indepyear").textValue() == "1919"
            data.get(0).get("gnpold").textValue() == null
    }


}
