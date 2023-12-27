package com.nikozka.app.service;

import com.nikozka.app.dto.ProductDTO;
import com.nikozka.app.entity.ProductEntity;
import com.nikozka.app.exceptions.DateNotParsedException;
import com.nikozka.app.repository.ProductRepository;
import com.nikozka.app.utils.ProductTableCreation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductTableCreation productTableCreation;

    public List<ProductDTO> getAllProducts(Pageable pageable) {
        Page<ProductEntity> userPage = productRepository.findAll(pageable);
        return userPage.map(this::convertToDTO).getContent();
    }

    @Transactional
    public void saveDynamicTable(String tableName, List<ProductDTO> records) {
        productTableCreation.createTableIfNotExists(tableName);
        for (ProductDTO product : records) {
            productRepository.saveAndFlush(convertToEntity(product));
        }
    }
    private ProductDTO convertToDTO(ProductEntity product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private ProductEntity convertToEntity(ProductDTO product) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate entryDate = LocalDate.parse(product.getEntryDate(), formatter);

            ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
            productEntity.setEntryDate(entryDate);
            return productEntity;
        } catch (DateTimeParseException e) {
            throw new DateNotParsedException("Incorrect date value. Expected format: dd-MM-yyyy");
        }
    }

}
