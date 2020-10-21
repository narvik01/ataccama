package cz.strangeloop.ataccama.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ColumnDto {

    private String name;
    private String defaultValue;
    private String columnDataType;
    private boolean autoIncremented;
    private boolean generated;
    private boolean hidden;
    private boolean partOfForeignKey;
    private boolean partOfPrimaryKey;
    private boolean partOfUniqueIndex;

}
