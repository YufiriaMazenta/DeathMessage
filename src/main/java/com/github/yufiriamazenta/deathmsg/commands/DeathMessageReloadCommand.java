package com.github.yufiriamazenta.deathmsg.commands;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.data.DataContainer;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
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
        String message = DeathMessage.INSTANCE.getConfig().getString("command_reload");
        commandSender.sendMessage(LangUtil.color(message));
        return true;
    }
}
