package com.nikozka.app.utils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class UserTableCreation {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS user_entity (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(255) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL" +
                ");";

        entityManager.createNativeQuery(createTableQuery).executeUpdate();
    }
}