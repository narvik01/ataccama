package cz.strangeloop.ataccama.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class TableConstraintDto {

    private List<String> columns;
    private String type;

}
