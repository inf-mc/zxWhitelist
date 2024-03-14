package com.zhuoxin.whitelist.util;

import java.sql.*;

public class DatabaseInitializer {
    // 初始化数据库和表
    public static void initializeDatabase() {
        // 创建数据库连接
        try (Connection connection = JdbcUtil.getConnection()) {
            if (connection != null) {
                // 创建数据库
                createDatabase(connection, JdbcUtil.databaseName);
                // 指定数据库
                connection.setCatalog( JdbcUtil.databaseName);
                // 创建 whitelist 表
                createWhitelistTable(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 创建数据库
    private static void createDatabase(Connection connection, String databaseName) throws SQLException {

        // 如果数据库不存在，则创建数据库
        if (!JdbcUtil.databaseExists(connection)) {
            String createDatabaseSQL = "CREATE DATABASE " + databaseName;
            try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL)) {
                statement.executeUpdate();
            }
        }
    }

    // 创建 whitelist 表
    private static void createWhitelistTable(Connection connection) throws SQLException {
        // 检查指定数据库中表是否存在
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(JdbcUtil.databaseName, null, "whitelist", null);
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
}
