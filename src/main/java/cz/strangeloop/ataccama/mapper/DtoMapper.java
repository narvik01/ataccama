package cz.strangeloop.ataccama.mapper;

import cz.strangeloop.ataccama.api.dto.PostgresDBConnectionDto;
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostgresDBConnectionDto mapConnection(PostgresDBConnection connection);
    PostgresDBConnection mapConnection(PostgresDBConnectionDto connection);
    List<PostgresDBConnectionDto> mapConnections(List<PostgresDBConnection> connection);

}
