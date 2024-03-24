package com.zhuoxin.whitelist;

import com.zhuoxin.whitelist.command.CommandDeal;
import com.zhuoxin.whitelist.dbtool.DatabaseInitialize;
import com.zhuoxin.whitelist.dbtool.DatabaseOperate;
import com.zhuoxin.whitelist.dbtool.JdbcUtil;
import com.zhuoxin.whitelist.listener.PlayerJoinListener;
import com.zhuoxin.whitelist.websever.WebSever;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Whitelist extends JavaPlugin {
    JdbcUtil jdbcUtil = new JdbcUtil(this);
    DatabaseInitialize databaseInitialize = new DatabaseInitialize(this, jdbcUtil);
    DatabaseOperate databaseOperate = new DatabaseOperate(this, jdbcUtil);
    WebSever webSever = new WebSever(this,databaseOperate);
    Logger logger = getLogger();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        if (!databaseConfigOK()) {
            logger.log(Level.WARNING, "请检查配置文件 'zxWhitelist/config.yml' 数据库部分");
        }
        if (!databaseInitialize.initializeDatabase()) {
            logger.log(Level.WARNING, "请检查配置文件 'zxWhitelist/config.yml' 数据库部分");
        }
        registerListeners();
        getCommands();
        logger.log(Level.INFO, "zxWhitelist插件加载结束");
    }

    boolean databaseConfigOK() {
        List<String> databaseConfig = jdbcUtil.getDatabaseConfig();
        boolean noEmptyValue = databaseConfig.stream().noneMatch(value -> value == null || value.isEmpty());
        if (noEmptyValue) {
            if (!databaseConfig.get(2).matches(Objects.requireNonNull(getConfig().getString("database.rules.dbName")))) {
                logger.log(Level.WARNING, "数据库名不合法");
                return false;
            } else
                return true;
        }
        return false;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this, jdbcUtil), this);
    }

    private void getCommands() {
        Objects.requireNonNull(getCommand("whitelistaddplayer")).setExecutor(new CommandDeal(this, databaseOperate));
        Objects.requireNonNull(getCommand("whitelistdeleteplayer")).setExecutor(new CommandDeal(this, databaseOperate));
        Objects.requireNonNull(getCommand("whitelistinquire")).setExecutor(new CommandDeal(this, databaseOperate));
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().disablePlugin(this); // 取消注册所有监听器
        logger.log(Level.INFO, "zxWhitelist插件已被禁用");
    }
}
