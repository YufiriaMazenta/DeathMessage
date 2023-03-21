package com.github.yufiriamazenta.deathmsg;

import com.github.yufiriamazenta.deathmsg.commands.DeathMessageReloadCommand;
import com.github.yufiriamazenta.deathmsg.data.DataContainer;
import com.github.yufiriamazenta.deathmsg.listener.JoinQuitHandler;
import com.github.yufiriamazenta.deathmsg.listener.DeathHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

public class DeathMessage extends JavaPlugin {

    public static Server server;
    public static String version;
    public static Logger LOGGER;
    public static Plugin plugin;
    public static Random random;

    @Override
    public void onDisable() {
        getLogger().info("DeathMessage Disabled");
        server = null;
        version = null;
        LOGGER = null;
    }

    @Override
    public void onEnable() {
        getLogger().info("DeathMessage Enabled");
        saveDefaultConfig();
        server = getServer();
        version = getServer().getClass().getPackage().getName().split("\\.")[3];
        System.out.println("Load for version " + version);
        LOGGER = getLogger();
        plugin = this;
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
        } else {
            getLogger().info("set command 'deathmessagereload' executor failed");
        }
    }

    public void initDataContainer() {

        DataContainer.reloadMap();

    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
