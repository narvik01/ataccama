package cz.strangeloop.ataccama.service;

import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IntrospectionService {

    List<Schema> getSchemas(UUID id);

    List<Table> getTables(UUID id, String schema);

    List<Column> getColumns(UUID id, String schema, String table);

    List<Map<String, String>> getDataPreview(UUID id, String schema, String table, int count);
}
