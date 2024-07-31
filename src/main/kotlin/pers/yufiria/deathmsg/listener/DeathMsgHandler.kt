package pers.yufiria.deathmsg.listener

import crypticlib.listener.EventListener
import net.md_5.bungee.api.ChatMessageType
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.event.DeathMessageSendEvent
import pers.yufiria.deathmsg.util.PlayerUtil

@EventListener
object DeathMsgHandler: Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun sendDeathMsg(event: DeathMessageSendEvent) {
        if (event.isCancelled) return
        val deathMessage = event.deathMessage()
        val deadPlayer = event.deadPlayer()
        val chatMessageTypeStr = Configs.deathMessageType.value().uppercase()
        val chatMessageType = if (chatMessageTypeStr != "ALL") {
            try {
                ChatMessageType.valueOf(chatMessageTypeStr)
            } catch (e: Exception) {
                ChatMessageType.CHAT
            }
        } else {
            null
        }

        //发送死亡消息给没有屏蔽死亡消息的玩家
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (deadPlayer != onlinePlayer) {
                if (PlayerUtil.isPlayerDeathMsgFilterOn(onlinePlayer)) continue
            }
            when (chatMessageType) {
                ChatMessageType.CHAT, ChatMessageType.SYSTEM -> {
                    onlinePlayer.sendMessage(deathMessage)
                }
                ChatMessageType.ACTION_BAR -> {
                    onlinePlayer.sendActionBar(deathMessage)
                }
                null -> {
                    onlinePlayer.sendMessage(deathMessage)
                    onlinePlayer.sendActionBar(deathMessage)
                }
            }
        }

        Bukkit.getConsoleSender().sendMessage(deathMessage)
    }

}