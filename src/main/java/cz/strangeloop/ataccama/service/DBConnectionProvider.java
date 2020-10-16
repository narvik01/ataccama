package cz.strangeloop.ataccama.service;

import cz.strangeloop.ataccama.db.DBConnectionRepository;
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Lazy;
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
            //TODO proper handling
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void invalidate(UUID uuid) {
        DataSource removedDS = dataSourceMap.remove(uuid);
        if (removedDS == null) {
            throw new NotFoundException();
        }
    }

    private DataSource createDataSource(UUID uuid) {
        Optional<PostgresDBConnection> optionalConnection = dbConnectionRepository.findById(uuid);
        PostgresDBConnection postgresDBConnection = optionalConnection.orElseThrow(NotFoundException::new);
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
