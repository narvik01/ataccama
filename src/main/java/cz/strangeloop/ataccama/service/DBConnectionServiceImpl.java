package cz.strangeloop.ataccama.service;

import cz.strangeloop.ataccama.db.DBConnectionRepository;
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DBConnectionServiceImpl implements DBConnectionService {

    private final DBConnectionRepository dbConnectionRepository;

    @Override
    public List<PostgresDBConnection> list() {
        return dbConnectionRepository.findAll();
    }

    @Override
    public PostgresDBConnection find(UUID connectionId) {
        Optional<PostgresDBConnection> byId = dbConnectionRepository.findById(connectionId);
        return byId.orElseThrow(NotFoundException::new);
    }

    @Override
    public UUID create(PostgresDBConnection postgresDBConnection) {
        PostgresDBConnection newConnection = dbConnectionRepository.save(postgresDBConnection);
        return newConnection.getId();
    }

    @Override
    public void delete(UUID id) {
        dbConnectionRepository.deleteById(id);
    }

    @Override
    public void update(UUID id, PostgresDBConnection postgresDBConnection) {
        Optional<PostgresDBConnection> loadedConnection = dbConnectionRepository.findById(id);
        if (loadedConnection.isEmpty()) {
            throw new NotFoundException();
        }

        postgresDBConnection.setId(id); //do not allow to change id by the body
        dbConnectionRepository.save(postgresDBConnection);
    }
}
