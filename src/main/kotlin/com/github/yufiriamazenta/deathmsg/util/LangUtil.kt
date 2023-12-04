package com.github.yufiriamazenta.deathmsg.util

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import crypticlib.util.MsgUtil
import org.bukkit.command.CommandSender

object LangUtil {

    fun sendLang(receiver: CommandSender?, msgKey: String?) {
        sendLang(receiver, msgKey, null)
    }

    fun sendLang(receiver: CommandSender?, msgKey: String?, formatMap: Map<String, String>?) {
        val message = DEATH_MESSAGE.config.getString(msgKey!!, msgKey)
        MsgUtil.sendMsg(receiver!!, message, formatMap?: HashMap<String, String>())
    }

}