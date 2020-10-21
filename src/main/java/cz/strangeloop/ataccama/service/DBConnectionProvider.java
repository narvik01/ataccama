package cz.strangeloop.ataccama.service;

import cz.strangeloop.ataccama.db.DBConnectionRepository;
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DBConnectionProvider {

    private final DBConnectionRepository dbConnectionRepository;
    private final Map<UUID, DataSource> dataSourceMap = new HashMap<>();

    @Value("${db.driver.class:org.postgresql.Driver}")
    private String driverClassName;
    @Value("${db.jdbc.prefix:postgresql}")
    private String jdbcPrefix;

    public Connection getConnection(UUID uuid) {
        DataSource dataSource = dataSourceMap.computeIfAbsent(uuid, this::createDataSource);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if (PSQLState.INVALID_PASSWORD.getState().equals(sqlState)) {
                throw new CredentialsException(e);
            }
            throw new UnknownDBException(e);
        }
    }

    public void invalidate(UUID uuid) {
        dataSourceMap.remove(uuid);
    }

    private DataSource createDataSource(UUID uuid) {
        Optional<PostgresDBConnection> optionalConnection = dbConnectionRepository.findById(uuid);
        PostgresDBConnection postgresDBConnection = optionalConnection.orElseThrow(() -> new NotFoundException("Connection with this id does not exist."));
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(constructJdbcUrl(postgresDBConnection))
                .username(postgresDBConnection.getUsername())
                .password(postgresDBConnection.getPassword())
                .build();
    }

    protected String constructJdbcUrl(PostgresDBConnection postgresDBConnection) {
        return "jdbc:" + jdbcPrefix + "://" + postgresDBConnection.getHostname() + ":" + postgresDBConnection.getPort() + "/" + postgresDBConnection.getDatabaseName();
    }
}
