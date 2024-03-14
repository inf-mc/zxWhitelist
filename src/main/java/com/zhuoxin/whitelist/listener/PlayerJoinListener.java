package com.zhuoxin.whitelist.listener;

import com.zhuoxin.whitelist.Whitelist;
import com.zhuoxin.whitelist.dbtool.DatabaseOperate;
import com.zhuoxin.whitelist.dbtool.JdbcUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerJoinListener implements Listener {
    private final Whitelist whitelist;
    private final JdbcUtil jdbcUtil;
    private final DatabaseOperate databaseOperate;
    public PlayerJoinListener(Whitelist whitelist, JdbcUtil jdbcUtil) {
        this.whitelist = whitelist;
        this.jdbcUtil = jdbcUtil;
        this.databaseOperate = new DatabaseOperate(this.whitelist, this.jdbcUtil);

    }

    @EventHandler
    public void beforePlayerJoin(AsyncPlayerPreLoginEvent loggingEvent) {
        String loggingPlayerName = loggingEvent.getName();
        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + loggingPlayerName + "正在尝试加入世界");
        //如果该玩家在白名单中
        if (databaseOperate.nameExists(loggingPlayerName)) {
            //允许加入
            loggingEvent.allow();
        }
        //否则
        else {
            //拒绝加入并提示原因
            loggingEvent.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "你还不在白名单中");
            //公告该玩家不在白名单
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "但是" + loggingPlayerName + "还不在白名单中");
        }
    }
}
