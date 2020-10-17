package cz.strangeloop.ataccama.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class PriviledgeDto {
    List<String> grants;
    String name;
}
