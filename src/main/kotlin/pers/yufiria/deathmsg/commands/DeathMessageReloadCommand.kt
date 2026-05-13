package pers.yufiria.deathmsg.commands

import crypticlib.chat.BukkitMsgSender
import crypticlib.command.BukkitCommand
import crypticlib.command.CommandInfo
import crypticlib.command.CommandInvoker
import crypticlib.command.CommandTree
import crypticlib.command.annotation.Command
import crypticlib.perm.PermInfo
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.yufiria.deathmsg.DEATH_MESSAGE
import pers.yufiria.deathmsg.config.Configs

@Command
object DeathMessageReloadCommand : CommandTree(
    CommandInfo(
        "deathmessagereload",
        PermInfo("deathmessage.command.reload"),
        mutableListOf("dmrl")
    )
) {

    override fun execute(invoker: CommandInvoker, args: MutableList<String>) {
        if (args.isNotEmpty()) {
            return
        }
        if (invoker.isPlayer && !invoker.hasPermission("deathmessage.command.reload")) {
            return
        }
        DEATH_MESSAGE.reloadPlugin()
        invoker.sendMsg(Configs.pluginMessageCommandReload.value())
    }

}
