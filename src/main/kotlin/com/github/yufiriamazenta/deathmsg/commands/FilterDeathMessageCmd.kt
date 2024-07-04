package com.github.yufiriamazenta.deathmsg.commands

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import com.github.yufiriamazenta.deathmsg.util.LangUtil
import crypticlib.command.CommandHandler
import crypticlib.command.CommandInfo
import crypticlib.command.annotation.Command
import crypticlib.perm.PermInfo
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

@Command
object FilterDeathMessageCmd : CommandHandler(CommandInfo("deathmessagefilter", PermInfo("deathmessage.command.filter"), arrayOf("dmf"))) {

    override fun execute(sender: CommandSender, args: MutableList<String>): Boolean {
        if (sender !is Player) {
            LangUtil.sendLang(sender, "plugin_message.only_player")
            return true
        }
        if (args.isEmpty()) {
            toggleFilter(sender)
        } else {
            when (args[0]) {
                "on" -> setFilterOn(sender)
                "off" -> setFilterOff(sender)
                else -> toggleFilter(sender)
            }
        }
        return true
    }

    override fun tab(sender: CommandSender, args: MutableList<String>): MutableList<String> {
        return when (args.size) {
            0, 1 -> {
                val list: MutableList<String> = mutableListOf("off", "on")
                list.removeIf { str: String -> !str.startsWith(args[0]) }
                list
            }

            else -> {
                mutableListOf("")
            }
        }
    }

    private fun toggleFilter(player: Player) {
        val filterFlag = DEATH_MESSAGE.isPlayerDeathMsgFilterOn(player)
        if (filterFlag) {
            setFilterOff(player)
        } else {
            setFilterOn(player)
        }
    }

    private fun setFilterOn(player: Player) {
        val dataContainer = player.persistentDataContainer
        dataContainer[DEATH_MESSAGE.deathMessageFilterKey, PersistentDataType.BYTE] = 1.toByte()
        LangUtil.sendLang(player, "plugin_message.filter_on")
    }

    private fun setFilterOff(player: Player) {
        val dataContainer = player.persistentDataContainer
        dataContainer[DEATH_MESSAGE.deathMessageFilterKey, PersistentDataType.BYTE] = 0.toByte()
        LangUtil.sendLang(player, "plugin_message.filter_off")
    }

}
