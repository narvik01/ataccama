package cz.strangeloop.ataccama.service;

import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;

import java.util.List;
import java.util.UUID;

public interface DBConnectionService {

    List<PostgresDBConnection> list();

    PostgresDBConnection find(UUID connectionId);

    UUID create(PostgresDBConnection postgresDBConnection);

    void delete(UUID id);

    void update(UUID id, PostgresDBConnection postgresDBConnection);
}
