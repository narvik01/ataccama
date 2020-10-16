package cz.strangeloop.ataccama

import cz.strangeloop.ataccama.db.DBConnectionRepository
import cz.strangeloop.ataccama.service.DBConnectionProvider
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestSpringConfiguration {

    @Bean
    DBConnectionProvider dbConnectionProvider(DBConnectionRepository dbConnectionRepository) {
        return new TestDBConnectionProvider(dbConnectionRepository)
    }

}
