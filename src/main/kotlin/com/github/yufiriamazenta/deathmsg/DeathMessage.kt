package com.github.yufiriamazenta.deathmsg

import com.github.yufiriamazenta.deathmsg.data.DataManager
import crypticlib.BukkitPlugin
import crypticlib.util.MsgUtil
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class DeathMessage: BukkitPlugin() {

    val deathMessageFilterKey = NamespacedKey(this, "death_msg_filter")

    override fun enable() {
        saveDefaultConfig()
        initDataContainer()
        MsgUtil.info("[DeathMessage] DeathMessage Enabled")
    }

    override fun disable() {
        MsgUtil.info("[DeathMessage] DeathMessage Disabled")
    }

    private fun initDataContainer() {
        DataManager.reloadData()
    }

    fun isPlayerDeathMsgFilterOn(player: Player): Boolean {
        val dataContainer = player.persistentDataContainer
        val filterSwitch = dataContainer[DEATH_MESSAGE.deathMessageFilterKey, PersistentDataType.BYTE]
        return filterSwitch != null && filterSwitch.toInt() != 0
    }

}

val DEATH_MESSAGE: DeathMessage = (Bukkit.getPluginManager().getPlugin("DeathMessage") as DeathMessage?)!!