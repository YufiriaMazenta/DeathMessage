package pers.yufiria.deathmsg.sync

import com.google.common.io.ByteStreams
import crypticlib.CrypticLibPlugin
import crypticlib.lifecycle.Lifecycle
import crypticlib.lifecycle.LifecycleRule
import crypticlib.lifecycle.LifecycleTask
import crypticlib.lifecycle.LifecycleTaskSettings
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.event.DeathMessageSendEvent
import pers.yufiria.deathmsg.util.ComponentUtil
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException

@LifecycleTaskSettings(rules = [LifecycleRule(lifeCycle = Lifecycle.ENABLE)])
object DataReceiver: PluginMessageListener, LifecycleTask {

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

    override fun lifecycle(plugin: CrypticLibPlugin, lifecycle: Lifecycle) {
        if (Configs.proxy.value()) {
            val pluginIns: JavaPlugin = plugin as JavaPlugin
            pluginIns.server.messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")
            pluginIns.server.messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this)
        }
    }


}