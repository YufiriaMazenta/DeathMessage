package com.github.yufiriamazenta.deathmsg.commands;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
import crypticlib.command.IPluginCommand;
import crypticlib.command.ISubCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class FilterDeathMessageCmd implements IPluginCommand {

    private static final NamespacedKey filterKey = new NamespacedKey(DeathMessage.INSTANCE, "death_msg_filter");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) {
            LangUtil.msg(commandSender, "plugin_message.only_player");
            return true;
        }
        if (strings.length == 0) {
            toggleFilter(player);
        } else {
            switch (strings[0]) {
                case "on" -> setFilterOn(player);
                case "off" -> setFilterOff(player);
                default -> toggleFilter(player);
            }
        }
        return true;
    }

    public void toggleFilter(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        Byte val = dataContainer.get(filterKey, PersistentDataType.BYTE);
        if (val == null) {
            setFilterOn(player);
            return;
        }
        if (val == 0) {
            setFilterOn(player);
        } else {
            setFilterOff(player);
        }
    }

    public void setFilterOn(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.set(filterKey, PersistentDataType.BYTE, (byte)1);
        LangUtil.msg(player, "plugin_message.filter_on");
    }

    public void setFilterOff(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.set(filterKey, PersistentDataType.BYTE, (byte)0);
        LangUtil.msg(player, "plugin_message.filter_off");
    }

    public static NamespacedKey getFilterKey() {
        return filterKey;
    }

    @Override
    public Plugin getPlugin() {
        return DeathMessage.getInstance();
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        switch (strings.length) {
            case 0, 1 -> {
                List<String> list = new ArrayList<>(List.of("off", "on"));
                list.removeIf(str -> !str.startsWith(strings[0]));
                return list;
            }default -> {
                return Collections.singletonList("");
            }
        }
    }

    @Override
    public Map<String, ISubCommand> subCommands() {
        return new HashMap<>();
    }
}
