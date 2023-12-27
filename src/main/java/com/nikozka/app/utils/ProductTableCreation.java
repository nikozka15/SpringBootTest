package com.nikozka.app.utils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class ProductTableCreation {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createTableIfNotExists(String tableName) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "entry_date DATE NOT NULL," +
                "item_code INT NOT NULL," +
                "item_name VARCHAR(255) NOT NULL," +
                "item_quantity INT NOT NULL," +
                "status VARCHAR(255) NOT NULL" +
                ");";

        entityManager.createNativeQuery(createTableQuery).executeUpdate();
    }
}