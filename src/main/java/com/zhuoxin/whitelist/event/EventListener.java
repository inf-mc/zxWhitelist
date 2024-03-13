package com.zhuoxin.whitelist.event;

import com.zhuoxin.whitelist.util.dbOperate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class EventListener implements Listener {

    @EventHandler
    //监听玩家尝试加入世界的事件
    public void beforePlayerJoin(AsyncPlayerPreLoginEvent loggingEvent) {
        //获取玩家游戏名称
        String loggingPlayerName = loggingEvent.getName();
        //公告该玩家尝试加入世界
        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + loggingPlayerName + " is trying to joining the world");
        //如果该玩家在白名单中
        if (dbOperate.isNameExists(loggingPlayerName)) {
            //允许加入
            loggingEvent.allow();
        }
        //否则
        else {
            //拒绝加入并提示原因
            loggingEvent.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "you are not in whitelist");
            //公告该玩家不在白名单
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "But " + loggingPlayerName + " is not in the whitelist");
        }
    }
}
