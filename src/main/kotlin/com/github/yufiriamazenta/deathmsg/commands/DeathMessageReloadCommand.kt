package com.github.yufiriamazenta.deathmsg.commands

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import com.github.yufiriamazenta.deathmsg.data.DataManager.reloadData
import crypticlib.chat.MessageSender
import crypticlib.command.BukkitCommand
import crypticlib.command.RootCmdExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@BukkitCommand(name = "deathmessagereload", permission = "deathmessage.command.reload", aliases = ["dmrl"])
class DeathMessageReloadCommand : RootCmdExecutor() {

    override fun onCommand(
        commandSender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (args.isNotEmpty()) {
            return false
        }
        if (commandSender is Player && !commandSender.isOp()) {
            return false
        }
        reloadData()
        val message: String = DEATH_MESSAGE.getConfig().getString("plugin_message.command_reload")?: "DeathMessage Reloaded"
        MessageSender.sendMsg(commandSender, message)
        return true
    }

}
