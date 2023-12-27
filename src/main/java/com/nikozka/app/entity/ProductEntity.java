package com.nikozka.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Setter
    @Getter
    @Column(name = "item_code")
    private String itemCode;

    @Setter
    @Getter
    @Column(name = "item_name")
    private String itemName;

    @Setter
    @Getter
    @Column(name = "item_quantity")
    private String itemQuantity;

    @Setter
    @Getter
    @Column(name = "status")
    private String status;

    public ProductEntity() {
    }

    public ProductEntity(LocalDate entryDate, String itemCode, String itemName, String itemQuantity, String status) {
        this.entryDate = entryDate;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.status = status;
    }
}
