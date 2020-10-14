package cz.strangeloop.ataccama.service;

import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DBConnectionServiceImpl implements DBConnectionService {

    @Override
    public List<PostgresDBConnection> list() {
        return null;
    }

    @Override
    public PostgresDBConnection find(UUID connectionId) {
        return null;
    }

    @Override
    public UUID create(PostgresDBConnection postgresDBConnection) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public void update(UUID id, PostgresDBConnection postgresDBConnection) {

    }
}
