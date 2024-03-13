package com.zhuoxin.whitelist.command;


import com.zhuoxin.whitelist.Whitelist;
import com.zhuoxin.whitelist.util.dbOperate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDeal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // 如果命令发送者有这个命令的权限
        if (commandSender.hasPermission("Whitelist.commands.whitelistaddplayer")) {
            // 判断匹配的命令
            if (command.getName().equalsIgnoreCase("whitelistaddplayer")) {
                // 判断是否发送了用户名
                if (strings.length == 1) {
                    // 判断用户名是否合法
                    if (strings[0].matches(Whitelist.userNameRule)) {
                        // 执行插入，返回布尔类型的结果
                        if (dbOperate.insertName(strings[0])) {
                            commandSender.sendMessage(command.getName() + " " + strings[0]);
                            commandSender.sendMessage("done");
                            return true;
                        } else {
                            commandSender.sendMessage(command.getName() + " " + strings[0]);
                            commandSender.sendMessage("insert failed");
                            return false;
                        }
                    } else {
                        commandSender.sendMessage(command.getName() + " " + strings[0]);
                        commandSender.sendMessage("userName is illegal");
                    }
                } else {
                    commandSender.sendMessage(command.getName() + " " + strings[0]);
                    commandSender.sendMessage("wrong format");
                    return false;
                }
            }
            // 判断匹配的命令
            if (command.getName().equalsIgnoreCase("whitelistdeleteplayer")) {
                if (strings.length == 1) {
                    if (dbOperate.deleteName(strings[0])) {
                        commandSender.sendMessage(command.getName() + " " + strings[0]);
                        commandSender.sendMessage("done");
                        return true;
                    } else {
                        commandSender.sendMessage(command.getName() + " " + strings[0]);
                        commandSender.sendMessage("delete failed");
                        return false;
                    }
                } else {
                    commandSender.sendMessage(command.getName() + " " + strings[0]);
                    commandSender.sendMessage("wrong format");
                    return false;
                }
            }
            //判断匹配的命令
            if (command.getName().equalsIgnoreCase("whitelistinquire")) {
                commandSender.sendMessage(command.getName());
                commandSender.sendMessage(dbOperate.selectAll().toString());
                return true;
            }
            commandSender.sendMessage("wrong command");
            return false;
        }
        commandSender.sendMessage("no permission");
        return false;
    }
}
