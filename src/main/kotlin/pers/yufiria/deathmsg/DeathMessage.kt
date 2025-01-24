package pers.yufiria.deathmsg

import crypticlib.BukkitPlugin
import crypticlib.chat.BukkitMsgSender
import org.bukkit.Bukkit

class DeathMessage: BukkitPlugin() {


    override fun enable() {
        BukkitMsgSender.INSTANCE.info("[DeathMessage] DeathMessage Enabled")
    }

    override fun disable() {
        BukkitMsgSender.INSTANCE.info("[DeathMessage] DeathMessage Disabled")
    }

}

val DEATH_MESSAGE: DeathMessage = (Bukkit.getPluginManager().getPlugin("DeathMessage") as DeathMessage?)!!