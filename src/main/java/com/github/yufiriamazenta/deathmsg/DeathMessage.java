package com.github.yufiriamazenta.deathmsg;

import com.github.yufiriamazenta.deathmsg.commands.DeathMessageReloadCommand;
import com.github.yufiriamazenta.deathmsg.commands.FilterDeathMessageCmd;
import com.github.yufiriamazenta.deathmsg.data.DataContainer;
import com.github.yufiriamazenta.deathmsg.listener.JoinQuitHandler;
import com.github.yufiriamazenta.deathmsg.listener.DeathHandler;
import crypticlib.BukkitPlugin;
import crypticlib.util.MsgUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.Random;

public class DeathMessage extends BukkitPlugin {

    public static Server server;
    public static String version;
    public static DeathMessage INSTANCE;
    public static Random random;

    @Override
    public void disable() {
        getLogger().info("DeathMessage Disabled");
        server = null;
        version = null;
    }

    @Override
    public void enable() {
        getLogger().info("DeathMessage Enabled");
        saveDefaultConfig();
        server = getServer();
        version = getServer().getClass().getPackage().getName().split("\\.")[3];
        MsgUtil.info("[DeathMessage] Load for version " + version);
        INSTANCE = this;
        random = new Random();

        initDataContainer();
        initEventHandler();
        initCommandExecutor();
    }

    public void initEventHandler() {
        Bukkit.getPluginManager().registerEvents(DeathHandler.INSTANCE, this);
        Bukkit.getPluginManager().registerEvents(JoinQuitHandler.INSTANCE, this);
    }

    public void initCommandExecutor() {
        if (Bukkit.getPluginCommand("deathmessagereload") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("deathmessagereload")).setExecutor(new DeathMessageReloadCommand());
            Bukkit.getPluginCommand("deathmessagefilter").setExecutor(FilterDeathMessageCmd.INSTANCE);
            Bukkit.getPluginCommand("deathmessagefilter").setTabCompleter(FilterDeathMessageCmd.INSTANCE);
        } else {
            getLogger().info("set command 'deathmessagereload' executor failed");
        }
    }

    public void initDataContainer() {

        DataContainer.reloadMap();

    }

    public static DeathMessage getInstance() {
        return INSTANCE;
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
