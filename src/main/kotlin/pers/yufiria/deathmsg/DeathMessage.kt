package pers.yufiria.deathmsg

import pers.yufiria.deathmsg.config.DeathMessages
import crypticlib.BukkitPlugin
import crypticlib.chat.BukkitMsgSender
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class DeathMessage: BukkitPlugin() {


    override fun enable() {
        BukkitMsgSender.INSTANCE.info("[DeathMessage] DeathMessage Enabled")
    }

    override fun disable() {
        BukkitMsgSender.INSTANCE.info("[DeathMessage] DeathMessage Disabled")
    }

}

val DEATH_MESSAGE: DeathMessage = (Bukkit.getPluginManager().getPlugin("DeathMessage") as DeathMessage?)!!