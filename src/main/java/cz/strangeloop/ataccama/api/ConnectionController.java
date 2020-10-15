package cz.strangeloop.ataccama.api;

import cz.strangeloop.ataccama.api.dto.PostgresDBConnectionDto;
import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
import cz.strangeloop.ataccama.mapper.DtoMapper;
import cz.strangeloop.ataccama.service.DBConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static cz.strangeloop.ataccama.api.ConnectionController.PATH;

@RestController
@RequestMapping(PATH)
@RequiredArgsConstructor
public class ConnectionController {

    static final String PATH = "/connections";

    private final DtoMapper dtoMapper;
    private final DBConnectionService dbConnectionService;

    @GetMapping
    public ResponseEntity<List<PostgresDBConnectionDto>> list() {
        List<PostgresDBConnection> postgresDBConnectionList = dbConnectionService.list();
        if (postgresDBConnectionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(dtoMapper.mapConnections(postgresDBConnectionList));
        }
    }

    @GetMapping("/{id}")
    public PostgresDBConnectionDto get(@PathVariable UUID id) {
        PostgresDBConnection postgresDBConnection = dbConnectionService.find(id);
        return dtoMapper.mapConnection(postgresDBConnection);
    }

    @PostMapping
    public ResponseEntity<Void> post(UriComponentsBuilder uriComponentBuilder, @RequestBody PostgresDBConnectionDto postgresDBConnection) {
        UUID id = dbConnectionService.create(dtoMapper.mapConnection(postgresDBConnection));
        URI location = uriComponentBuilder.path(PATH + "/{id}")
                .buildAndExpand(id.toString())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public void put(@PathVariable UUID id, @RequestBody PostgresDBConnectionDto postgresDBConnection) {
        dbConnectionService.update(id, dtoMapper.mapConnection(postgresDBConnection));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        dbConnectionService.delete(id);
    }

}
