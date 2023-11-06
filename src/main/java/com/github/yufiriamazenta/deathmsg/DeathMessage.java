package com.github.yufiriamazenta.deathmsg;

import com.github.yufiriamazenta.deathmsg.data.DataManager;
import crypticlib.BukkitPlugin;
import crypticlib.CrypticLib;
import crypticlib.util.MsgUtil;
import org.bukkit.NamespacedKey;

public class DeathMessage extends BukkitPlugin {

    public static DeathMessage INSTANCE;

    private NamespacedKey filterKey;

    @Override
    public void disable() {
        getLogger().info("DeathMessage Disabled");
    }

    @Override
    public void enable() {
        getLogger().info("DeathMessage Enabled");
        saveDefaultConfig();
        filterKey = new NamespacedKey(this, "death_msg_filter");
        MsgUtil.info("[DeathMessage] Load for version " + CrypticLib.nmsVersion());
        INSTANCE = this;
        initDataContainer();
    }

    public void initDataContainer() {
        DataManager.reloadMap();
    }

    public static DeathMessage getInstance() {
        return INSTANCE;
    }

    public NamespacedKey getDeathMsgFilterKey() {
        return filterKey;
    }

}
