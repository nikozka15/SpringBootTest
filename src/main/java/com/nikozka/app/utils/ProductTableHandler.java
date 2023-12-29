package com.nikozka.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class ProductTableHandler {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;  // todo

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
    public boolean isTableNotExists(String tableName) {

        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE UPPER(table_name) = UPPER(?)";

        int count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);

        return count <= 0;
    }
}