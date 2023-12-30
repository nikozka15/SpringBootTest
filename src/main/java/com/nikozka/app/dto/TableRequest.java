package com.nikozka.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Validated
public class TableRequest {

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Table name should contain only characters")
    @Size(min = 2, max = 255, message = "Table name must be between 2 and 255 characters")
    private String table;

    @NotEmpty(message = "Records list cannot be empty")
    private List<@Valid ProductDTO> records;
}
