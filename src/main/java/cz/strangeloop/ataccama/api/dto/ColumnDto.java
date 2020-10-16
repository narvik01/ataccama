package cz.strangeloop.ataccama.api.dto;

import lombok.Data;

@Data
public class ColumnDto {

    private String name;
    private String defaultValue;
    private String columnDataType;

}
