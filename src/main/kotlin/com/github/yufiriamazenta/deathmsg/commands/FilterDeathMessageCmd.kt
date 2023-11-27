package com.github.yufiriamazenta.deathmsg.commands

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import com.github.yufiriamazenta.deathmsg.util.LangUtil
import crypticlib.command.BukkitCommand
import crypticlib.command.impl.RootCmdExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

@BukkitCommand(name = "deathmessagefilter", aliases = ["dmf"], permission = "deathmessage.command.filter")
class FilterDeathMessageCmd : RootCmdExecutor() {
    override fun onCommand(commandSender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (commandSender !is Player) {
            LangUtil.sendLang(commandSender, "plugin_message.only_player")
            return true
        }
        if (args.isEmpty()) {
            toggleFilter(commandSender)
        } else {
            when (args[0]) {
                "on" -> setFilterOn(commandSender)
                "off" -> setFilterOff(commandSender)
                else -> toggleFilter(commandSender)
            }
        }
        return true
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        s: String,
        strings: Array<String>
    ): List<String> {
        return when (strings.size) {
            0, 1 -> {
                val list: MutableList<String> = mutableListOf("off", "on")
                list.removeIf { str: String -> !str.startsWith(strings[0]) }
                list
            }

            else -> {
                listOf("")
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
