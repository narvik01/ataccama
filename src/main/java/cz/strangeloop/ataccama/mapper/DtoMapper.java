package cz.strangeloop.ataccama.mapper;

import cz.strangeloop.ataccama.api.dto.*;
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import schemacrawler.schema.*;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostgresDBConnectionDto mapConnection(PostgresDBConnection connection);

    PostgresDBConnection mapConnection(PostgresDBConnectionDto connection);

    List<PostgresDBConnectionDto> mapConnections(List<PostgresDBConnection> connection);

    List<SchemaDto> mapSchemas(List<Schema> schemas);

    @Mapping(target = "type", source = "constraintType")
    TableConstraintDto mapTableConstraint(TableConstraint tc);

    List<TableConstraintDto> mapTableConstraints(Collection<TableConstraint> tc);

    default String mapTableConstraintColumn(TableConstraintColumn tcc) {
        return tcc.getName();
    }

    default String mapIndexColumn(IndexColumn ic) {
        return ic.getName();
    }

    default String mapGrant(Grant<?> grant) {
        return grant.getGrantee();
    }


    @Mapping(target = "constraints", source = "tableConstraints")
    @Mapping(target = "grants", source = "privileges")
    @Mapping(target = "primaryKey", source = "primaryKey.columns")
    @Mapping(target = "tableType", source = "tableType.tableType")
    TableDto mapTable(Table table);

    List<TableDto> mapTables(List<Table> tables);

    @Mapping(target = "columnDataType", source = "columnDataType.name")
    ColumnDto mapColumn(Column col);

    List<ColumnDto> mapColumns(List<Column> columns);

}
