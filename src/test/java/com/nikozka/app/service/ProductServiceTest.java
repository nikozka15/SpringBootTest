package com.nikozka.app.service;

import com.nikozka.app.dto.ProductDTO;
import com.nikozka.app.entity.ProductEntity;
import com.nikozka.app.exceptions.DateNotParsedException;
import com.nikozka.app.repository.ProductRepository;
import com.nikozka.app.utils.ProductTableHandler;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductTableHandler productTableHandler;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProductsTestSuccess() {
        Pageable pageable = Pageable.unpaged();
        Page<ProductEntity> emptyPage = new PageImpl<>(List.of(getEntity()), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(emptyPage);
        when(modelMapper.map(any(ProductEntity.class), eq(ProductDTO.class))).thenReturn(mock(ProductDTO.class));

        List<ProductDTO> result = productService.getAllProducts(pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty())
        );
    }

    @Test
    void saveDynamicTableTestSuccess() {
        String tableName = "testTable";
        ProductDTO productDTO = new ProductDTO("01-01-2023", "123", "Test Item", "10", "Paid");
        List<ProductDTO> records = List.of(productDTO);

        ArgumentCaptor<ProductEntity> argumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);

        doNothing().when(productTableHandler).createTableIfNotExists(tableName);
        when(productRepository.saveAndFlush(argumentCaptor.capture())).thenReturn(getEntity());
        when(modelMapper.map(any(), eq(ProductEntity.class))).thenReturn(getEntity());

        productService.save(tableName, records);

        verify(productTableHandler, times(1)).createTableIfNotExists(tableName);
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

        doNothing().when(productTableHandler).createTableIfNotExists(tableName);

        assertThrows(DateNotParsedException.class, () -> productService.save(tableName, records));

        verify(productTableHandler, times(1)).createTableIfNotExists(tableName);
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
