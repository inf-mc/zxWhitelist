package com.zhuoxin.whitelist.dbtool;

import com.zhuoxin.whitelist.Whitelist;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class JdbcUtil {
    private final Whitelist whitelist;
    private final String driver;
    private final String url;
    private final String dbName;
    private final String user;
    private final String pw;

    public JdbcUtil(Whitelist whitelist) {
        this.whitelist = whitelist;
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.url = "jdbc:mysql://" + whitelist.getConfig().getString("database.host");
        this.dbName = Objects.requireNonNull(whitelist.getConfig().getString("database.dbName")).toLowerCase();
        this.user = whitelist.getConfig().getString("database.user");
        this.pw = whitelist.getConfig().getString("database.password");
    }

    public String getDbName() {
        return dbName;
    }

    public List<String> getDatabaseConfig() {
        return Arrays.asList(driver, url, dbName, user, pw);
    }

    public Connection getConnection() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, pw);
            if (databaseExists(connection)) {
                // 指定数据库
                connection.setCatalog(dbName);
            }
            return connection;
        } catch (Exception e) {
            whitelist.getLogger().log(Level.WARNING, "数据库连接失败", e);
        }
        return null;
    }

    public boolean databaseExists(Connection connection) {
        boolean databaseExists = false;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getCatalogs();
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(dbName)) {
                    databaseExists = true;
                    break;
                }
            }
            resultSet.close();
        } catch (Exception e) {
            whitelist.getLogger().log(Level.WARNING, "检查数据库是否存在时发生错误", e);
        }
        return databaseExists;
    }

}
