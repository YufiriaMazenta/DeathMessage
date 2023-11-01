package com.github.yufiriamazenta.deathmsg;

import com.github.yufiriamazenta.deathmsg.commands.DeathMessageReloadCommand;
import com.github.yufiriamazenta.deathmsg.commands.FilterDeathMessageCmd;
import com.github.yufiriamazenta.deathmsg.data.DataManager;
import com.github.yufiriamazenta.deathmsg.listener.DeathHandler;
import crypticlib.BukkitPlugin;
import crypticlib.CrypticLib;
import crypticlib.util.MsgUtil;
import org.bukkit.Bukkit;

import java.util.Objects;

public class DeathMessage extends BukkitPlugin {

    public static DeathMessage INSTANCE;

    @Override
    public void disable() {
        getLogger().info("DeathMessage Disabled");
    }

    @Override
    public void enable() {
        getLogger().info("DeathMessage Enabled");
        saveDefaultConfig();
        MsgUtil.info("[DeathMessage] Load for version " + CrypticLib.nmsVersion());
        INSTANCE = this;

        initDataContainer();
        initEventHandler();
        initCommandExecutor();
    }

    public void initEventHandler() {
        Bukkit.getPluginManager().registerEvents(DeathHandler.INSTANCE, this);
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
        DataManager.reloadMap();
    }

    public static DeathMessage getInstance() {
        return INSTANCE;
    }

}
