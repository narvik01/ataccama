package cz.strangeloop.ataccama.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class TableDto {

    private String name;
    private String tableType;
    private List<String> primaryKey;
    private List<TableConstraintDto> constraints;

    private List<PriviledgeDto> grants;
    private List<IndexDto> indexes;

}
