package com.zhuoxin.whitelist.command;

import com.zhuoxin.whitelist.Whitelist;
import com.zhuoxin.whitelist.dbtool.DatabaseOperate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class CommandDeal implements CommandExecutor {
    private final Whitelist whitelist ;
    private final DatabaseOperate databaseOperate;

    public CommandDeal(Whitelist whitelist, DatabaseOperate databaseOperate) {
        this.whitelist = whitelist;
        this.databaseOperate = databaseOperate;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // 检查权限
        if (!commandSender.hasPermission("Whitelist.commands.whitelistaddplayer")) {
            commandSender.sendMessage("没有权限");
            return false;
        }
        // 判断命令名称
        if (command.getName().equalsIgnoreCase("whitelistaddplayer") && strings.length == 1)
            return processAddPlayerCommand(commandSender, strings[0]);
        if (command.getName().equalsIgnoreCase("whitelistdeleteplayer") && strings.length == 1)
            return processDeletePlayerCommand(commandSender, strings[0]);
        if (command.getName().equalsIgnoreCase("whitelistinquire"))
            return processInquireCommand(commandSender);
        commandSender.sendMessage("指令错误");
        return false;
    }

    private boolean processAddPlayerCommand(CommandSender commandSender, String playerName) {
        // 判断用户名是否合法
        if (!playerName.matches(Objects.requireNonNull(whitelist.getConfig().getString("database.rules.userName")))) {
            commandSender.sendMessage("用户名不合法");
            return false;
        }
        if (databaseOperate.insertName(playerName)) {
            commandSender.sendMessage("添加成功");
            return true;
        }
        return false;
    }

    private boolean processDeletePlayerCommand(CommandSender commandSender, String playerName) {
        if (databaseOperate.deleteName(playerName)) {
            commandSender.sendMessage("删除成功");
            return true;
        }
        return false;
    }

    private boolean processInquireCommand(CommandSender commandSender) {
        commandSender.sendMessage(databaseOperate.selectAll().toString());
        return true;
    }
}
