package com.github.yufiriamazenta.deathmsg.commands;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.data.DataManager;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
import crypticlib.annotations.BukkitCommand;
import crypticlib.command.IPluginCommand;
import crypticlib.command.ISubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@BukkitCommand(name = "deathmessagereload", perm = "deathmessage.command.reload", alias = {"dmrl"})
public class DeathMessageReloadCommand implements IPluginCommand {

    @Override
    public Plugin getPlugin() {
        return DeathMessage.getInstance();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0) {
            return false;
        }
        if ((commandSender instanceof Player) && !commandSender.isOp()) {
            return false;
        }
        DataManager.reloadMap();
        String message = DeathMessage.INSTANCE.getConfig().getString("plugin_message.command_reload");
        commandSender.sendMessage(LangUtil.color(message));
        return true;
    }

    @Override
    public Map<String, ISubCommand> subCommands() {
        return new HashMap<>();
    }
}
