package com.mingxin.commands;

import com.mingxin.data.DataContainer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

public class DeathMessageReloadCommand implements CommandExecutor {
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0) {
            return false;
        }
        if ((commandSender instanceof Player) && !commandSender.isOp()) {
            return false;
        }
        DataContainer.reloadMap();
        commandSender.sendMessage(ChatColor.GREEN + "DeathMessage reload complete");
        return true;
    }
}
