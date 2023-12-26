package com.nikozka.app.service;

import com.nikozka.app.dto.ProductDTO;
import com.nikozka.app.entity.ProductEntity;
import com.nikozka.app.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProductDTO> getAllProducts(Pageable pageable) {
        Page<ProductEntity> userPage = productRepository.findAll(pageable);
        return userPage.map(this::convertToDTO).getContent();
    }

    private ProductDTO convertToDTO(ProductEntity user) {
        return modelMapper.map(user, ProductDTO.class);
    }
}
