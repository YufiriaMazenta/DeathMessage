package pers.yufiria.deathmsg

import crypticlib.BukkitPlugin
import crypticlib.chat.BukkitMsgSender
import crypticlib.util.IOHelper
import org.bukkit.Bukkit

class DeathMessage: BukkitPlugin() {


    override fun whenEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHooker.register()
            IOHelper.info("PlaceholderExpansion registered")
        }
        BukkitMsgSender.INSTANCE.info("DeathMessage Enabled")
    }

    override fun whenDisable() {
        BukkitMsgSender.INSTANCE.info("DeathMessage Disabled")
    }

}

val DEATH_MESSAGE: DeathMessage = (Bukkit.getPluginManager().getPlugin("DeathMessage") as DeathMessage?)!!