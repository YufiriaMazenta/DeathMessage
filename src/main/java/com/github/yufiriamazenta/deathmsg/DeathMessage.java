package com.github.yufiriamazenta.deathmsg;

import com.github.yufiriamazenta.deathmsg.data.DataManager;
import crypticlib.BukkitPlugin;
import crypticlib.CrypticLib;
import crypticlib.util.MsgUtil;

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
    }

    public void initDataContainer() {
        DataManager.reloadMap();
    }

    public static DeathMessage getInstance() {
        return INSTANCE;
    }

}
