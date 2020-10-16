package cz.strangeloop.ataccama

import cz.strangeloop.ataccama.db.DBConnectionRepository
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection
import cz.strangeloop.ataccama.service.DBConnectionProvider

/**
 * Connection provider with testing data included in test container
 */
class TestDBConnectionProvider extends DBConnectionProvider {
    TestDBConnectionProvider(DBConnectionRepository dbConnectionRepository) {
        super(dbConnectionRepository)
    }

    @Override
    protected String constructJdbcUrl(PostgresDBConnection postgresDBConnection) {
        return super.constructJdbcUrl(postgresDBConnection) //TODO + "?TC_INITSCRIPT=file:data/iso-3166.sql"
    }
}
