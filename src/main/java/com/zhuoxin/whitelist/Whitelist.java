package com.zhuoxin.whitelist;

import com.zhuoxin.whitelist.command.CommandDeal;
import com.zhuoxin.whitelist.event.EventListener;
import com.zhuoxin.whitelist.util.DatabaseInitializer;
import com.zhuoxin.whitelist.util.JdbcUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Whitelist extends JavaPlugin {

    public static FileConfiguration config;

    public static void useConsoleOutput(String text) {
        System.out.println(text);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // 注册监听器
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        // 申请命令
        getCommand("whitelistaddplayer").setExecutor(new CommandDeal());
        getCommand("whitelistdeleteplayer").setExecutor(new CommandDeal());
        getCommand("whitelistinquire").setExecutor(new CommandDeal());
        saveDefaultConfig();
        // 读取配置信息
        config = getConfig();
        JdbcUtil.setConfig(config.getString("driver"), config.getString("url"), config.getString("databaseName"),
                config.getString("user"), config.getString("password"));
        //检查数据库状态
        DatabaseInitializer.initializeDatabase();
        //提示插件加载成功
        useConsoleOutput("Plugin load success!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
