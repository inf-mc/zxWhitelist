package com.zhuoxin.whitelist.dbtool;

import com.zhuoxin.whitelist.Whitelist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DatabaseOperate {
    private final Whitelist whitelist;
    private final JdbcUtil jdbcUtil;

    public DatabaseOperate(Whitelist whitelist, JdbcUtil jdbcUtil) {
        this.whitelist = whitelist;
        this.jdbcUtil = jdbcUtil;
    }

    public Boolean insertName(String name) {
        try (Connection connection = jdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into whitelist(name) values(?)")) {
            // 给预处理对象的参数赋值
            statement.setString(1, name);
            // 执行 SQL 语句并获取更新行数
            int rowsAffected = statement.executeUpdate();
            // 根据更新行数判断是否插入成功
            return rowsAffected > 0;
        } catch (SQLException e) {
            logInsertError(e);
        }
        return false;
    }

    // 查询名单中是否存在指定姓名
    public boolean nameExists(String name) {
        String sql = "SELECT COUNT(*) FROM whitelist WHERE name = ?";
        try (Connection connection = jdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logSelectError(e);
        }
        return false;
    }

    // 删除名单中的指定姓名
    public boolean deleteName(String name) {
        String sql = "DELETE FROM whitelist WHERE name = ?";
        try (Connection connection = jdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logDeleteError(e);
        }
        return false;
    }

    //查询全部白名单玩家
    public List<String> selectAll() {
        String sql = "SELECT name FROM whitelist";
        List<String> whitelistPlayers = new ArrayList<>();
        try (Connection connection = jdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            // 遍历结果集，将白名单玩家添加到列表中
            while (resultSet.next()) {
                String playerName = resultSet.getString("name");
                whitelistPlayers.add(playerName);
            }
        } catch (SQLException e) {
            logSelectError(e);
        }
        return whitelistPlayers;
    }

    void logInsertError(SQLException e) {
        // 获取异常的 SQL 状态码
        String sqlState = e.getSQLState();
        // 根据 SQL 状态码输出到日志
        if (sqlState != null) {
            switch (sqlState) {
                case "23000":
                    whitelist.getLogger().log(Level.SEVERE, "发生唯一约束冲突，插入数据失败");
                    break;
                case "42S02":
                    whitelist.getLogger().log(Level.SEVERE, "数据库表不存在，插入数据失败");
                    break;
                default:
                    whitelist.getLogger().log(Level.SEVERE, "发生 SQL 异常，插入数据失败");
            }
        } else {
            // 如果无法获取 SQL 状态码，则直接输出异常信息到日志
            whitelist.getLogger().log(Level.SEVERE, "发生 SQL 异常，插入数据失败");
        }
    }

    void logSelectError(SQLException e) {
        // 获取异常的 SQL 状态码
        String sqlState = e.getSQLState();
        // 根据 SQL 状态码输出到日志
        if (sqlState != null) {
            if (sqlState.equals("42S22")) {
                whitelist.getLogger().log(Level.SEVERE, "查询的列不存在，查询数据失败");
            } else {
                whitelist.getLogger().log(Level.SEVERE, "发生 SQL 异常，查询数据失败");
            }
        } else {
            // 如果无法获取 SQL 状态码，则直接输出异常信息到日志
            whitelist.getLogger().log(Level.SEVERE, "发生 SQL 异常，查询数据失败");
        }
    }

    void logDeleteError(SQLException e) {
        // 获取异常的 SQL 状态码
        String sqlState = e.getSQLState();
        // 根据 SQL 状态码输出到日志
        if (sqlState != null) {
            if (sqlState.equals("23000")) {
                whitelist.getLogger().log(Level.SEVERE, "违反唯一约束，删除数据失败");
            } else {
                whitelist.getLogger().log(Level.SEVERE, "发生 SQL 异常，删除数据失败");
            }
        } else {
            // 如果无法获取 SQL 状态码，则直接输出异常信息到日志
            whitelist.getLogger().log(Level.SEVERE, "发生 SQL 异常，删除数据失败");
        }
    }
}
