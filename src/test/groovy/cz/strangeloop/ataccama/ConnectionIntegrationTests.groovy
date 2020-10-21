package cz.strangeloop.ataccama

import cz.strangeloop.ataccama.api.dto.PostgresDBConnectionDto
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification
import spock.lang.Stepwise

@SpringBootTest(classes = AtaccamaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Testcontainers
@Stepwise
@DirtiesContext
@SuppressFBWarnings(value = "SE_NO_SERIALVERSIONID", justification = "serialVersionUID not generated in closures")
class ConnectionIntegrationTests extends Specification {

    public static final String PATH = "/connections"

    @Autowired
    TestRestTemplate rt

    def "list no connections"() {
        when: "loading a list of connections after start"
            def entity = rt.getForEntity(PATH, List.class)
        then: "no content is returned"
            entity.statusCode == HttpStatus.NO_CONTENT
    }

    def "add connection"() {
        given:
            def name = "external1"
        when: "added a connection"
            def location = rt.postForLocation(PATH, [
                    name        : name,
                    hostname    : "external_database_1",
                    port        : 5432,
                    databaseName: "externaldb",
                    username    : "external",
                    password    : "extpass"
            ])
        then: "location is present"
            location != null
        and: "can be accessed"
            def object = rt.getForObject(location, PostgresDBConnectionDto.class)
             object.name == name
        and: "connection is in list"
            def list = rt.getForObject(PATH, PostgresDBConnectionDto[].class)
            list.count {it.name = name } == 1
    }

    def "get unknown id"() {
        when: "trying to load not existing id"
            def entity = rt.getForEntity("$PATH/00000000-0000-0000-0000-000000000000", PostgresDBConnectionDto.class)
        then: "404 not found"
            entity.statusCode == HttpStatus.NOT_FOUND
    }

    def "update a connection"() {
        given: "added connection"
            def location = rt.postForLocation(PATH, [
                    name        : "wrongname",
                    hostname    : "wronghost",
                    port        : 99,
                    databaseName: "wrongdb",
                    username    : "wronguser",
                    password    : "wrongpass"
            ])
        when: "connection is updated"
            rt.put(location, [
                    name        : "external2",
                    hostname    : "external_database_2",
                    port        : 5432,
                    databaseName: "externaldb2",
                    username    : "external2",
                    password    : "extpass2"
            ])
        then: "data is updated"
            def list = rt.getForObject(PATH, PostgresDBConnectionDto[].class)
            list.length == 2
            list.count {it.name == 'external2'} == 1
            list.count {it.name == 'wrongname'} == 0
    }

    def "update unknown id"() {
        when: "trying to update not existing id"
            def entity = rt.exchange(new RequestEntity<PostgresDBConnectionDto>([
                    name        : "external2",
                    hostname    : "external_database_2",
                    port        : 5432,
                    databaseName: "externaldb2",
                    username    : "external2",
                    password    : "extpass2"
            ], HttpMethod.PUT, "$PATH/00000000-0000-0000-0000-000000000000".toURI()), Void.class)
        then: "404 not found"
            entity.statusCode == HttpStatus.NOT_FOUND
    }


    def "delete a connection"() {
        given: "added connection"
            def location = rt.postForLocation(PATH, [
                    name        : "wrongname2",
                    hostname    : "wronghost",
                    port        : 99,
                    databaseName: "wrongdb",
                    username    : "wronguser",
                    password    : "wrongpass"
            ])
        when: "connection is deleted"
            rt.delete(location)
        then: "object is not present"
            def list = rt.getForObject(PATH, PostgresDBConnectionDto[].class)
            list.length == 2
            list.count {it.name == 'wrongname2'} == 0
    }

    def "delete unknown id"() {
        when: "trying to delete not existing id"
            def entity = rt.exchange(new RequestEntity<Object>(HttpMethod.DELETE, "$PATH/00000000-0000-0000-0000-000000000000".toURI()), PostgresDBConnectionDto.class)
        then: "404 not found"
            entity.statusCode == HttpStatus.NOT_FOUND
    }

}
