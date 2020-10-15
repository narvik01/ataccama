package cz.strangeloop.ataccama.api.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class PostgresDBConnectionDto {

    private UUID id;
    private String name;
    private String hostname;
    private int port;
    private String databaseName;
    private String username;
    private String password;

}
