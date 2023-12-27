package com.nikozka.app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ProductDTO {
    @NotNull(message = "Entry date cannot be null")
//    @Pattern(
//            regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$",
//            message = "Invalid date format. Use dd-MM-yyyy."
//    )
    private String entryDate;

    @NotNull(message = "Item code cannot be null")
    @Pattern(regexp = "\\d+", message = "Item code must be a numeric value")
    private String itemCode;

    @NotNull(message = "Item name cannot be null")
    @Size(min = 3, max = 255, message = "Item name must be between 3 and 255 characters")
    private String itemName;

    @NotNull(message = "Item quantity cannot be null")
    @Pattern(regexp = "\\d+", message = "Item quantity must be a numeric value")
    private String itemQuantity;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^(Paid|Unpaid)$", message = "Status must be 'Paid' or 'Unpaid'")
    private String status;

    public ProductDTO() {
    }

    public ProductDTO(String entryDate, String itemCode, String itemName, String itemQuantity, String status) {
        this.entryDate = entryDate;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.status = status;
    }
}
