package com.github.yufiriamazenta.deathmsg.commands

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import com.github.yufiriamazenta.deathmsg.data.DataManager.reloadData
import crypticlib.command.api.BukkitCommand
import crypticlib.command.impl.RootCmdExecutor
import crypticlib.util.MsgUtil
import crypticlib.util.TextUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import javax.annotation.ParametersAreNonnullByDefault

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
        MsgUtil.sendMsg(commandSender, message)
        return true
    }

}
