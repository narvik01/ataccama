package cz.strangeloop.ataccama.mapper;

import cz.strangeloop.ataccama.api.dto.*;
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostgresDBConnectionDto mapConnection(PostgresDBConnection connection);
    PostgresDBConnection mapConnection(PostgresDBConnectionDto connection);
    List<PostgresDBConnectionDto> mapConnections(List<PostgresDBConnection> connection);

    List<SchemaDto> mapSchemas(List<Schema> schemas);
    List<TableDto> mapTables(List<Table> tables);

    @Mapping(target = "columnDataType", source = "columnDataType.name")
    ColumnDto mapColumn(Column col);

    List<ColumnDto> mapColumns(List<Column> columns);

}
