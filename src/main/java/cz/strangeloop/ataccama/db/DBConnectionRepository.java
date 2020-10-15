package cz.strangeloop.ataccama.db;

import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DBConnectionRepository extends JpaRepository<PostgresDBConnection, UUID> {

}
