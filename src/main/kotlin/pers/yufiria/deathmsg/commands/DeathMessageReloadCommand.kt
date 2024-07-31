package pers.yufiria.deathmsg.commands

import crypticlib.chat.BukkitMsgSender
import crypticlib.command.BukkitCommand
import crypticlib.command.CommandInfo
import crypticlib.command.annotation.Command
import crypticlib.perm.PermInfo
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.yufiria.deathmsg.DEATH_MESSAGE
import pers.yufiria.deathmsg.config.Configs

@Command
object DeathMessageReloadCommand : BukkitCommand(CommandInfo("deathmessagereload", PermInfo("deathmessage.command.reload"), mutableListOf("dmrl"))) {

    override fun execute(sender: CommandSender, args: MutableList<String>) {
        if (args.isNotEmpty()) {
            return
        }
        if (sender is Player && !sender.isOp()) {
            return
        }
        DEATH_MESSAGE.reloadPlugin()
        BukkitMsgSender.INSTANCE.sendMsg(sender, Configs.pluginMessageCommandReload.value())
    }

}
