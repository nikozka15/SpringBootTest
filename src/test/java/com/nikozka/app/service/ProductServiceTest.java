package com.nikozka.app.service;

import com.nikozka.app.dto.ProductDTO;
import com.nikozka.app.entity.ProductEntity;
import com.nikozka.app.exceptions.DateNotParsedException;
import com.nikozka.app.repository.ProductRepository;
import com.nikozka.app.utils.ProductTableCreation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductTableCreation productTableCreation;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProductsTestSuccess() {
        Pageable pageable = Pageable.unpaged();
        Page<ProductEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        List<ProductDTO> result = productService.getAllProducts(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void saveDynamicTableTestSuccess() {
        String tableName = "testTable";
        ProductDTO productDTO = new ProductDTO("01-01-2023", "123", "Test Item", "10", "Paid");
        List<ProductDTO> records = List.of(productDTO);

        ArgumentCaptor<ProductEntity> argumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);

        doNothing().when(productTableCreation).createTableIfNotExists(tableName);
        when(productRepository.saveAndFlush(argumentCaptor.capture())).thenReturn(getEntity());
        when(modelMapper.map(any(), eq(ProductEntity.class))).thenReturn(getEntity());

        productService.saveDynamicTable(tableName, records);

        verify(productTableCreation, times(1)).createTableIfNotExists(tableName);
        verify(productRepository, times(1)).saveAndFlush(any());

        ProductEntity capturedArgument = argumentCaptor.getValue();

        Assertions.assertAll("ProductEntity properties",
                () -> Assertions.assertNotNull(capturedArgument, "Result should not be null"),
                () -> Assertions.assertEquals(capturedArgument.getEntryDate(), getEntity().getEntryDate(), "EntryDate should match")
        );
    }

    @Test
    void saveDynamicTableTestDateNotParsedException() {
        String tableName = "testTable";
        ProductDTO productDTO = new ProductDTO("incorrect", "123", "Test Item", "10", "Paid");
        List<ProductDTO> records = List.of(productDTO);

        doNothing().when(productTableCreation).createTableIfNotExists(tableName);

        assertThrows(DateNotParsedException.class, () -> productService.saveDynamicTable(tableName, records));

        verify(productTableCreation, times(1)).createTableIfNotExists(tableName);
        verify(productRepository, times(0)).saveAndFlush(any());
    }

    private ProductEntity getEntity() {
        return  new ProductEntity(
                LocalDate.of(2023, 1, 1),
                "123",
                "Test Item",
                "10",
                "Paid");
    }
}
