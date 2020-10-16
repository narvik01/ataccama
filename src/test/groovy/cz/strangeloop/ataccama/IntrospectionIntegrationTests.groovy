package cz.strangeloop.ataccama

import cz.strangeloop.ataccama.api.dto.SchemaDto
import cz.strangeloop.ataccama.api.dto.TableDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@SpringBootTest(classes = [AtaccamaApplication.class, TestSpringConfiguration.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Testcontainers
@Stepwise
@DirtiesContext
class IntrospectionIntegrationTests extends Specification {

    //TODO invalid values tests

    private static final int DBPORT = 5432
    public static final String PATH = "/connections"

    @Autowired
    TestRestTemplate rt

    @Shared
    public static DockerComposeContainer dbContainer =
            new DockerComposeContainer(new File("docker-compose.db.yml"))
                    .withExposedService("main_database_1", DBPORT)
                    .withExposedService("external_database_1_1", DBPORT)
                    .withExposedService("external_database_2_1", DBPORT)

    def "get schemas and tables"() {
        given:
            String location = rt.postForLocation("/connections", [
                    name        : "external1",
                    hostname    : "external_database_1",
                    port        : 5432,
                    databaseName: "externaldb",
                    username    : "external",
                    password    : "extpass"
            ])
            def connectionId = location.tokenize('/')[-1]

        when: "lists of schemas and tables are loaded"
            def schemas = rt.getForObject("/introspection/$connectionId/schemas", SchemaDto[].class)
            def tables = rt.getForObject("/introspection/$connectionId/schemas/public/tables", TableDto[].class)
        then:
            schemas.length != 0
//            tables.length != 0
    }

}
