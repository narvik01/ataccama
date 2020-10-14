package cz.strangeloop.ataccama.domain.connection;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "connections")
public class PostgresDBConnection {

    @Id
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String hostname;
    @Min(0)
    @Max(65535)
    private int port;

    //TODO can credentials be empty?
    private String databaseName;
    private String username;
    private String password;

}
