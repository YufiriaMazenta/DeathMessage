package pers.yufiria.deathmsg.sync

import com.google.common.collect.Iterables
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import pers.yufiria.deathmsg.DEATH_MESSAGE
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.util.ComponentUtil
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

object DataSender {

    fun sendDeathMessage(deathMessage: Component) {
        if (!Configs.proxy.value())
            return
        try {
            sendByRandomPlayer(
                getOutput(
                    Symbols.sendDeathMessage,
                    ComponentUtil.serialize(deathMessage)
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    private fun getOutput(vararg messages: String): ByteArrayDataOutput? {
        val output = ByteStreams.newDataOutput()
        output.writeUTF("Forward")
        output.writeUTF("ALL")
        output.writeUTF(Symbols.channel)
        //写入数据
        val msgBytes = ByteArrayOutputStream()
        val msgOutput = DataOutputStream(msgBytes)
        for (message in messages) {
            msgOutput.writeUTF(message)
        }
        val msgByteArray = msgBytes.toByteArray()
        output.writeShort(msgByteArray.size)
        output.write(msgByteArray)
        return output
    }

    private fun sendByRandomPlayer(output: ByteArrayDataOutput?) {
        if (!Configs.proxy.value()) return
        val player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null)!!
        player.sendPluginMessage(DEATH_MESSAGE, "BungeeCord", output!!.toByteArray())
    }

}