package cz.strangeloop.ataccama.api;

import cz.strangeloop.ataccama.api.dto.ColumnDto;
import cz.strangeloop.ataccama.api.dto.SchemaDto;
import cz.strangeloop.ataccama.api.dto.TableDto;
import cz.strangeloop.ataccama.mapper.DtoMapper;
import cz.strangeloop.ataccama.service.IntrospectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cz.strangeloop.ataccama.api.ConnectionController.PATH;

/**
 * Introspection API, schemas, tables, columns, data
 */
@RestController
@RequestMapping(PATH)
@RequiredArgsConstructor
public class IntrospectionController {

    private final IntrospectionService introspectionService;
    private final DtoMapper dtoMapper;

    @GetMapping("/{id}/schemas")
    public List<SchemaDto> schemas(@PathVariable UUID id) {
        List<Schema> schemas = introspectionService.getSchemas(id);
        return dtoMapper.mapSchemas(schemas);
    }

    @GetMapping("/{id}/schemas/{schema}/tables")
    public List<TableDto> tables(@PathVariable UUID id, @PathVariable String schema) {
        List<Table> tables = introspectionService.getTables(id, schema);
        return dtoMapper.mapTables(tables);
    }

    @GetMapping("/{id}/schemas/{schema}/tables/{table}/columns")
    public List<ColumnDto> columns(@PathVariable UUID id, @PathVariable String schema, @PathVariable String table) {
        List<Column> columns = introspectionService.getColumns(id, schema, table);
        return dtoMapper.mapColumns(columns);
    }

    @GetMapping("/{id}/schemas/{schema}/tables/{table}/data")
    public List<Map<String, String>> dataPreview(@PathVariable UUID id, @PathVariable String schema,
                                                 @PathVariable String table,
                                                 @RequestParam(defaultValue = "10", required = false) int count) {
        return introspectionService.getDataPreview(id, schema, table, count);
    }

//    @GetMapping("/{id}/schemas/{schema}/tables/{table}/columnstats")
//    public StatisticsDto columnstats(@PathVariable UUID id, @PathVariable String schema, @PathVariable String table) {
//        TODO Single endpoint for statistics about each column: min, max, avg, median value of thecolumn
//        return List.of();
//    }

    //    @GetMapping("/{id}/schemas/{schema}/tablestats")
//    public StatisticsDto columnstats(@PathVariable UUID id, @PathVariable String schema) {
//        TODO Single endpoint for statistics about each table: number of records, number of attributes.
//        return List.of();
//    }
}
