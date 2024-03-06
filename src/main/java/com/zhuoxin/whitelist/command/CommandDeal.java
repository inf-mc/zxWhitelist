package com.zhuoxin.whitelist.command;


import com.zhuoxin.whitelist.util.dbOperate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDeal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("Whitelist.commands.whitelistaddplayer")) {
            if (command.getName().equalsIgnoreCase("whitelistaddplayer")) {
                if (strings.length == 1) {
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
                    commandSender.sendMessage("wrong format");
                    return false;
                }
            }
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
