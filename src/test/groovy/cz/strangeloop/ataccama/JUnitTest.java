//TODO remove

//package cz.strangeloop.ataccama;
//
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.DockerComposeContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.io.File;
//import java.util.List;
//
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension.class)
//@TestPropertySource("classpath:application-test.properties")
//@Testcontainers
//public class JUnitTest {
//
//    private static final int DBPORT = 5432;
//
//    @Autowired
//    TestRestTemplate rt;
//
//    @Container
//    public static DockerComposeContainer dbContainer =
//            new DockerComposeContainer(new File("docker-compose.db.yml"))
//                    .withExposedService("main_database_1", DBPORT)
//                    .withExposedService("external_database_1_1", DBPORT)
//                    .withExposedService("external_database_2_1", DBPORT);
//
//    @Test
//    public void test() {
//        Assertions.assertEquals(HttpStatus.NO_CONTENT, rt.getForEntity("/connections", List.class).getStatusCode());
//    }
//}
