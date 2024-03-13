package com.zhuoxin.whitelist.command;

import com.zhuoxin.whitelist.Whitelist;
import com.zhuoxin.whitelist.util.dbOperate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDeal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // 检查权限
        if (!commandSender.hasPermission("Whitelist.commands.whitelistaddplayer")) {
            commandSender.sendMessage("no permission");
            return false;
        }

        // 判断命令名称
        if (command.getName().equalsIgnoreCase("whitelistaddplayer") && strings.length == 1) {
            return processAddPlayerCommand(commandSender, strings[0]);
        } else if (command.getName().equalsIgnoreCase("whitelistdeleteplayer") && strings.length == 1) {
            return processDeletePlayerCommand(commandSender, strings[0]);
        } else if (command.getName().equalsIgnoreCase("whitelistinquire")) {
            return processInquireCommand(commandSender);
        } else {
            commandSender.sendMessage("wrong command");
            return false;
        }
    }

    private boolean processAddPlayerCommand(CommandSender commandSender, String playerName) {
        // 判断用户名是否合法
        if (playerName.matches(Whitelist.userNameRule)) {
            if (dbOperate.insertName(playerName)) {
                commandSender.sendMessage("done");
                return true;
            } else {
                commandSender.sendMessage("insert failed");
            }
        } else {
            commandSender.sendMessage("userName is illegal");
        }
        return false;
    }

    private boolean processDeletePlayerCommand(CommandSender commandSender, String playerName) {
        if (dbOperate.deleteName(playerName)) {
            commandSender.sendMessage("done");
            return true;
        } else {
            commandSender.sendMessage("delete failed");
            return false;
        }
    }

    private boolean processInquireCommand(CommandSender commandSender) {
        commandSender.sendMessage(dbOperate.selectAll().toString());
        return true;
    }
}
