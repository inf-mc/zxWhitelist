package com.zhuoxin.whitelist.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//获取到db.properties文件中的数据库信息
public class JdbcUtil {
    //私有变量
    private static String driver;
    private static String url;
    static String databaseName;
    private static String user;
    private static String password;


    public static void setConfig(String driver, String url, String databaseName, String user, String password) {
        JdbcUtil.driver = driver;
        JdbcUtil.url = url;
        JdbcUtil.databaseName = databaseName;
        JdbcUtil.user = user;
        JdbcUtil.password = password;
    }

    //返回数据库连接
    public static Connection getConnection() {
        try {
            //注册数据库的驱动
            Class.forName(driver);
            //获取数据库连接（里面内容依次是：主机名和端口、用户名、密码）
            Connection connection = DriverManager.getConnection(url, user, password);
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
            // 如果数据库存在，则指定数据库
            if (databaseExists) {
                connection.setCatalog(databaseName);
            }
            //返回数据库连接
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}