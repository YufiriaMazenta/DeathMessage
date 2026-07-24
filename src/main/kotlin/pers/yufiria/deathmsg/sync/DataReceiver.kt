package pers.yufiria.deathmsg.sync

import com.google.common.io.ByteStreams
import crypticlib.lifecycle.LifeCycle
import crypticlib.lifecycle.LifeCycleTask
import crypticlib.lifecycle.LifeCycleTaskSettings
import crypticlib.lifecycle.TaskRule
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.event.DeathMessageSendEvent
import pers.yufiria.deathmsg.util.ComponentUtil
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException

@LifeCycleTaskSettings(rules = [TaskRule(lifeCycle = LifeCycle.ENABLE)])
object DataReceiver: PluginMessageListener, LifeCycleTask {

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

    override fun lifecycle(plugin: Any, p1: LifeCycle) {
        if (Configs.proxy.value()) {
            val pluginIns: JavaPlugin = plugin as JavaPlugin
            pluginIns.server.messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")
            pluginIns.server.messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this)
        }
    }


}