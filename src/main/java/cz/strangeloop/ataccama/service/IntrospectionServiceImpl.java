package cz.strangeloop.ataccama.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.utility.SchemaCrawlerUtility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class IntrospectionServiceImpl implements IntrospectionService {

    private final DBConnectionProvider dbConnectionProvider;

    @Override
    public List<Schema> getSchemas(UUID id) {
        SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions();
        try {
            Catalog catalog = SchemaCrawlerUtility.getCatalog(dbConnectionProvider.getConnection(id), options);
            Collection<Schema> schemas = catalog.getSchemas();
            return new ArrayList<>(schemas);
        } catch (SchemaCrawlerException e) {
            e.printStackTrace();
            //TODO handle exception properly
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Table> getTables(UUID id, String schema) {
        SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions();
        try {
            Catalog catalog = SchemaCrawlerUtility.getCatalog(dbConnectionProvider.getConnection(id), options);
            Optional<Schema> optionalSchema = catalog.getSchemas().stream().filter(s -> s.getName().equals(schema)).findAny();
            Schema foundSchema = optionalSchema.orElseThrow(NotFoundException::new);
            Collection<Table> tables = catalog.getTables(foundSchema);
            return new ArrayList<>(tables);
        } catch (SchemaCrawlerException e) {
            e.printStackTrace();
            //TODO handle exception properly
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Column> getColumns(UUID id, String schema, String table) {
        SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions();
        try {
            Catalog catalog = SchemaCrawlerUtility.getCatalog(dbConnectionProvider.getConnection(id), options);
            Optional<Schema> optionalSchema = catalog.getSchemas().stream().filter(s -> s.getName().equals(schema)).findAny();
            Schema foundSchema = optionalSchema.orElseThrow(NotFoundException::new); //todo
            Collection<Table> tables = catalog.getTables(foundSchema);
            Optional<Table> optionalTable = tables.stream().filter(t -> t.getName().equals(table)).findAny();
            Table foundTable = optionalTable.orElseThrow(NotFoundException::new);
            List<Column> columns = foundTable.getColumns();
            return new ArrayList<>(columns);
        } catch (SchemaCrawlerException e) {
            e.printStackTrace();
            //TODO handle exception properly
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, String>> getDataPreview(UUID id, String schema, String table, int count) {
        Connection connection = dbConnectionProvider.getConnection(id);
        try {
            connection.setSchema(schema);
            String query = "SELECT * FROM " + table + " LIMIT " + count;
            ResultSet rs = connection.createStatement().executeQuery(query);
            List<Map<String,String>> data = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> map = convertResultSetToMap(rs);
                data.add(map);
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            //todo
            throw new RuntimeException();
        }
    }

    private Map<String, String> convertResultSetToMap(ResultSet rs) throws SQLException {
        Map<String, String> map = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = rs.getObject(i);
            if (value == null) {
                map.put(columnName, null);
            } else {
                map.put(columnName, value.toString());
            }
        }
        return map;
    }

}
