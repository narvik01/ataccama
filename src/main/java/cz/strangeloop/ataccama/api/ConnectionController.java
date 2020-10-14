package cz.strangeloop.ataccama.api;

import cz.strangeloop.ataccama.domain.connection.PostgresDBConnection;
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

    //TODO use DTOs

    static final String PATH = "/connections";
    private final DBConnectionService dbConnectionService;

    @GetMapping
    public ResponseEntity<List<PostgresDBConnection>> list() {
        List<PostgresDBConnection> postgresDBConnectionList = dbConnectionService.list();
        if (postgresDBConnectionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(postgresDBConnectionList);
        }
    }

    @PostMapping
    public ResponseEntity<Void> post(UriComponentsBuilder uriComponentBuilder, @RequestBody PostgresDBConnection postgresDBConnection) {
        UUID id = dbConnectionService.create(postgresDBConnection);
        URI location = uriComponentBuilder.path(PATH + "/{id}")
                .buildAndExpand(id.toString())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(PATH + "/{id}")
    public void put(@RequestParam UUID id, @RequestBody PostgresDBConnection postgresDBConnection) {
        dbConnectionService.update(id, postgresDBConnection);
    }

    @DeleteMapping
    public void delete(@RequestParam UUID id) {
        dbConnectionService.delete(id);
    }


}
