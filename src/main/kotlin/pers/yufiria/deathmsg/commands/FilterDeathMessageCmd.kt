package pers.yufiria.deathmsg.commands

import crypticlib.chat.BukkitMsgSender
import crypticlib.command.BukkitCommand
import crypticlib.command.CommandInfo
import crypticlib.command.annotation.Command
import crypticlib.perm.PermInfo
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.util.PlayerUtil

@Command
object FilterDeathMessageCmd : BukkitCommand(
    CommandInfo(
        "deathmessagefilter",
        PermInfo("deathmessage.command.filter"), mutableListOf("dmf")
    )
) {

    override fun execute(sender: CommandSender, args: MutableList<String>) {
        if (sender !is Player) {
            BukkitMsgSender.INSTANCE.sendMsg(sender, Configs.pluginMessagePlayerOnly.value())
            return
        }
        if (args.isEmpty()) {
            PlayerUtil.toggleFilter(sender)
        } else {
            when (args[0]) {
                "on" -> PlayerUtil.setFilterOn(sender)
                "off" -> PlayerUtil.setFilterOff(sender)
                else -> PlayerUtil.toggleFilter(sender)
            }
        }
        return
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


}
