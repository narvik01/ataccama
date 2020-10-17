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

    //TODO check other schema than public
    //TODO invalid values tests

    @Autowired
    TestRestTemplate rt

    @Shared
    public static DockerComposeContainer dbContainer =
            new DockerComposeContainer(new File("docker-compose.yml"), new File("docker-compose.dbexamples.yml"))


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
            def schemas = rt.getForObject("/connections/$connectionId/schemas", SchemaDto[].class)
            def tables = rt.getForObject("/connections/$connectionId/schemas/public/tables", TableDto[].class)
        then:
            schemas.length != 0
//            tables.length != 0
    }

}
