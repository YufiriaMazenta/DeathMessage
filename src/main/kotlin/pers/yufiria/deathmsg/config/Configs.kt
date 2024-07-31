package pers.yufiria.deathmsg.config

import crypticlib.config.ConfigHandler
import crypticlib.config.node.impl.bukkit.BooleanConfig
import crypticlib.config.node.impl.bukkit.ConfigSectionConfig
import crypticlib.config.node.impl.bukkit.StringConfig

@ConfigHandler(path = "config.yml")
object Configs {

    @JvmStatic val proxy = BooleanConfig("proxy", false)
    @JvmStatic val playerNameFormat = StringConfig("player_name_format", "%player_displayname%")
    @JvmStatic val deathMessageType = StringConfig("death_message_type", "chat")
    @JvmStatic val deathMessages = ConfigSectionConfig("death_message")
    @JvmStatic val pluginMessageCommandReload = StringConfig("plugin_message.command_reload", "&a插件重载完毕")
    @JvmStatic val pluginMessagePlayerOnly = StringConfig("plugin_message.player_only", "&c只有玩家才能使用此命令")
    @JvmStatic val pluginMessageFilterOn = StringConfig("plugin_message.filter_on", "&a已开启死亡消息屏蔽")
    @JvmStatic val pluginMessageFilterOff = StringConfig("plugin_message.filter_off", "&a已关闭死亡消息屏蔽")

}