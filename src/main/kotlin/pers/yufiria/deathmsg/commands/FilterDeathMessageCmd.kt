package pers.yufiria.deathmsg.commands

import crypticlib.command.CommandInfo
import crypticlib.command.CommandInvoker
import crypticlib.command.CommandTree
import crypticlib.command.annotation.Command
import crypticlib.perm.PermInfo
import org.bukkit.entity.Player
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.util.PlayerUtil.setFilterOff
import pers.yufiria.deathmsg.util.PlayerUtil.setFilterOn
import pers.yufiria.deathmsg.util.PlayerUtil.toggleFilter

@Command
object FilterDeathMessageCmd : CommandTree(
    CommandInfo(
        "deathmessagefilter",
        PermInfo("deathmessage.command.filter"), mutableListOf("dmf")
    )
) {

    override fun execute(invoker: CommandInvoker, args: MutableList<String>) {
        if (invoker.isConsole) {
            invoker.sendMsg(Configs.pluginMessagePlayerOnly.value())
            return
        }
        val player = invoker.asPlayer().platformPlayer as Player
        if (args.isEmpty()) {
            player.toggleFilter()
        } else {
            when (args[0]) {
                "on" -> player.setFilterOn()
                "off" -> player.setFilterOff()
                else -> player.toggleFilter()
            }
        }
        return
    }

    override fun tab(invoker: CommandInvoker, args: MutableList<String>): MutableList<String> {
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


}
