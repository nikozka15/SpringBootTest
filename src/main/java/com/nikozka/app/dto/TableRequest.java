package com.nikozka.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Validated
public class TableRequest {
    private String table;

    @NotEmpty(message = "Records list cannot be empty")
    private List<@Valid ProductDTO> records;
}
