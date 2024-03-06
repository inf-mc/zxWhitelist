package com.zhuoxin.whitelist.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dbOperate {
    public static Boolean insertName(String name) {
        // 使用 try-with-resources 语句，自动关闭资源
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into whitelist(name) values(?)")) {
            if (!isNameExists(name)) {
                // 给预处理对象的参数赋值
                statement.setString(1, name);
                // 执行 SQL 语句并获取更新行数
                int rowsAffected = statement.executeUpdate();
                // 根据更新行数判断是否插入成功
                return rowsAffected > 0;
            } else return false;
        } catch (SQLException e) {
            // 处理 SQL 异常
            e.printStackTrace();
            return false;
        }
    }

    // 查询名单中是否存在指定姓名
    public static boolean isNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM whitelist WHERE name = ?";
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 删除名单中的指定姓名
    public static boolean deleteName(String name) {
        String sql = "DELETE FROM whitelist WHERE name = ?";
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //查询全部白名单玩家
    public static List<String> selectAll(){
        String sql = "SELECT name FROM whitelist";
        List<String> whitelistPlayers = new ArrayList<>();
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            // 遍历结果集，将白名单玩家添加到列表中
            while (resultSet.next()) {
                String playerName = resultSet.getString("name");
                whitelistPlayers.add(playerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return whitelistPlayers;
    }
}
