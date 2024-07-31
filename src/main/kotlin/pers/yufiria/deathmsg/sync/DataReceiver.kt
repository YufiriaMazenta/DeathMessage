package pers.yufiria.deathmsg.sync

import com.google.common.io.ByteStreams
import crypticlib.lifecycle.BukkitEnabler
import crypticlib.lifecycle.annotation.OnEnable
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.PluginMessageListener
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.event.DeathMessageSendEvent
import pers.yufiria.deathmsg.util.ComponentUtil
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException

@OnEnable
object DataReceiver: PluginMessageListener, BukkitEnabler {

    override fun onPluginMessageReceived(channel: String, player: Player, messages: ByteArray) {
        if (!Configs.proxy.value()) return
        if (channel != "BungeeCord") {
            return
        }
        val input = ByteStreams.newDataInput(messages)
        val subChannel = input.readUTF()
        if (subChannel != Symbols.channel) return
        val len = input.readShort()
        val data = ByteArray(len.toInt())
        input.readFully(data)

        val dataInput = DataInputStream(ByteArrayInputStream(data))
        try {
            val symbol = dataInput.readUTF()
            when (symbol) {
                Symbols.sendDeathMessage -> {
                    val componentStr = dataInput.readUTF()
                    val deathMessage = ComponentUtil.deserialize(componentStr)
                    DeathMessageSendEvent(deathMessage, null).callEvent()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    override fun enable(plugin: Plugin) {
        if (Configs.proxy.value()) {
            plugin.server.messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")
            plugin.server.messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this)
        }
    }


}