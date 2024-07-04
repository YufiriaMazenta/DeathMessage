package com.github.yufiriamazenta.deathmsg.commands

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import com.github.yufiriamazenta.deathmsg.data.DataManager.reloadData
import crypticlib.chat.MsgSender
import crypticlib.command.CommandHandler
import crypticlib.command.CommandInfo
import crypticlib.command.annotation.Command
import crypticlib.perm.PermInfo
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command
object DeathMessageReloadCommand : CommandHandler(CommandInfo("deathmessagereload", PermInfo("deathmessage.command.reload"), arrayOf("dmrl"))) {

    override fun execute(sender: CommandSender, args: MutableList<String>): Boolean {
        if (args.isNotEmpty()) {
            return false
        }
        if (sender is Player && !sender.isOp()) {
            return false
        }
        reloadData()
        val message: String = DEATH_MESSAGE.config.getString("plugin_message.command_reload")?: "DeathMessage Reloaded"
        MsgSender.sendMsg(sender, message)
        return true
    }

}
