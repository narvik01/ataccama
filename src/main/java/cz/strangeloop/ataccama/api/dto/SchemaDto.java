package cz.strangeloop.ataccama.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SchemaDto {

    private String name;

}
