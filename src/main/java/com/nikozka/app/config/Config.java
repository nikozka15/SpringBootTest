package com.nikozka.app.config;

import com.nikozka.app.utils.ProductTableHandler;
import com.nikozka.app.utils.UserTableCreation;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserTableCreation userTableCreation() {
        return new UserTableCreation();
    }

    @Bean
    public ProductTableHandler productTableCreation() {
        return new ProductTableHandler();
    }
}
