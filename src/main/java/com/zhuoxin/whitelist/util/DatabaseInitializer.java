package com.zhuoxin.whitelist.util;

import java.sql.*;

public class DatabaseInitializer {
    // 初始化数据库和表
    public static void initializeDatabase() {
        // 创建数据库连接
        try (Connection connection = JdbcUtil.getConnection()) {
            if (connection != null) {
                // 创建数据库
                createDatabase(connection, "minecraft_whitelist");
                // 指定数据库
                connection.setCatalog("minecraft_whitelist");
                // 创建 whitelist 表
                createWhitelistTable(connection);
                // 创建 bind 表
                createBindTable(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 创建数据库
    private static void createDatabase(Connection connection, String databaseName) throws SQLException {
        // 检查数据库是否存在
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getCatalogs();
        boolean databaseExists = false;
        while (resultSet.next()) {
            String dbName = resultSet.getString(1);
            if (dbName.equals(databaseName)) {
                databaseExists = true;
                break;
            }
        }
        resultSet.close();
        // 如果数据库不存在，则创建数据库
        if (!databaseExists) {
            String createDatabaseSQL = "CREATE DATABASE " + databaseName;
            try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL)) {
                statement.executeUpdate();
            }
        }
    }

    // 创建 whitelist 表
    private static void createWhitelistTable(Connection connection) throws SQLException {
        // 检查表是否存在
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "whitelist", null);
        if (!resultSet.next()) {
            // 表不存在，创建表
            String createTableSQL = "CREATE TABLE whitelist (" +
                    "name VARCHAR(20) NOT NULL," +
                    "KEY idx_whitelist_name (name)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3";
            try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
                statement.executeUpdate();
            }
        }
    }

    // 创建 bind 表
    private static void createBindTable(Connection connection) throws SQLException {
        // 检查表是否存在
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "bind", null);
        if (!resultSet.next()) {
            // 表不存在，创建表
            String createTableSQL = "CREATE TABLE bind (" +
                    "name VARCHAR(20) NOT NULL," +
                    "source VARCHAR(20) DEFAULT NULL," +
                    "idNumber VARCHAR(100) DEFAULT NULL," +
                    "KEY fk_bind_name (name)," +
                    "CONSTRAINT fk_bind_name FOREIGN KEY (name) REFERENCES whitelist (name) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3";
            try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
                statement.executeUpdate();
            }
        }
    }
}