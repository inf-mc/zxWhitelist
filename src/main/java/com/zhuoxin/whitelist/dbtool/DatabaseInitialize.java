package com.zhuoxin.whitelist.dbtool;

import com.zhuoxin.whitelist.Whitelist;

import java.sql.*;
import java.util.logging.Level;

public class DatabaseInitialize {
    private final Whitelist whitelist;
    private final JdbcUtil jdbcUtil ;

    public DatabaseInitialize(Whitelist whitelist, JdbcUtil jdbcUtil) {
        this.whitelist = whitelist;
        this.jdbcUtil = jdbcUtil;
    }

    public boolean initializeDatabase() {
        try (Connection connection = jdbcUtil.getConnection()) {
            if (connection != null) {
                // 创建数据库
                createDatabase(connection, jdbcUtil.getDbName());
                // 指定数据库
                connection.setCatalog(jdbcUtil.getDbName());
                // 创建 whitelist 表
                createTable(connection);
                return true;
            }
            whitelist.getLogger().log(Level.WARNING, "数据库连接失败");
            return false;
        } catch (SQLException e) {
            whitelist.getLogger().log(Level.WARNING, "数据库或表创建失败");
            return false;
        }
    }

    private void createDatabase(Connection connection, String dbName) throws SQLException {
        // 如果数据库不存在，则创建数据库
        if (!jdbcUtil.databaseExists(connection)) {
            String createDatabaseSQL = "CREATE DATABASE " + dbName;
            try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL)) {
                statement.executeUpdate();
            }
        }
    }

    private void createTable(Connection connection) throws SQLException {
        // 检查指定数据库中表是否存在
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(jdbcUtil.getDbName(), null, "whitelist", null);
        // 表不存在，创建表
        if (!resultSet.next()) {
            String createTableSQL = "CREATE TABLE whitelist (" +
                    "name VARCHAR(20) NOT NULL PRIMARY KEY," +
                    "UNIQUE KEY idx_whitelist_name (name)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3";
            try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
                statement.executeUpdate();
            }
        }
    }
}
