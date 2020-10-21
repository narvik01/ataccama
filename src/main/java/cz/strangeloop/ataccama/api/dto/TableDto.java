package cz.strangeloop.ataccama.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TableDto {

    private String name;
    private String tableType;
    private List<String> primaryKey;
    private List<String> foreignKeys;
    private List<TableConstraintDto> constraints;

    private List<PriviledgeDto> grants;
    private List<IndexDto> indexes;

}
