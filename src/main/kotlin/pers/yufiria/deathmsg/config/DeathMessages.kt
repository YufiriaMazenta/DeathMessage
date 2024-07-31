package pers.yufiria.deathmsg.config

import crypticlib.lifecycle.BukkitEnabler
import crypticlib.lifecycle.BukkitReloader
import crypticlib.lifecycle.annotation.OnEnable
import crypticlib.lifecycle.annotation.OnReload
import crypticlib.util.YamlConfigHelper
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@OnEnable
@OnReload
object DeathMessages: BukkitReloader, BukkitEnabler {

    private val deathMsgMap: MutableMap<String, List<*>> = ConcurrentHashMap()
    private var random: Random = Random()

    fun addDeathMessage(deathCause: String, deathMessages: MutableList<String>) {
        val saveKey: String = deathCause.replace(".", "_")
        deathMsgMap[deathCause] = deathMessages
        Configs.deathMessages.value().set(saveKey, deathMessages)
        Configs.deathMessages.configContainer().configWrapper().saveConfig()
    }

    fun hasDeathCause(deathCause: String): Boolean {
        return deathMsgMap.containsKey(deathCause)
    }

    fun getMessage(player: Player, deathCause: String): String? {
        if (hasDeathCause(deathCause)) {
            val list = deathMsgMap[deathCause]
            if (list == null || list.isEmpty()) return null
            if (list[0] is Map<*, *>) {
                var messageList: List<String> = ArrayList()
                for (obj in list) {
                    val map = obj as Map<*, *>
                    if (!map.containsKey("perm") || player.hasPermission((map["perm"] as String?)!!)) {
                        messageList = map["message"] as List<String>
                        break
                    }
                }
                return if (messageList.isEmpty()) null else messageList[random.nextInt(messageList.size)]
            } else if (list[0] is String) {
                return list[random.nextInt(list.size)] as String?
            }
        }
        return null
    }

    override fun reload(plugin: Plugin) {
        deathMsgMap.clear()
        val deathMessagesConfig = Configs.deathMessages.value()
        for (key in deathMessagesConfig.getKeys(false)) {
            deathMsgMap[key.replace("_", ".")] = YamlConfigHelper.configList2List(
                deathMessagesConfig.getList(
                    key, ArrayList<Any>()
                )
            )
        }
    }

    override fun enable(plugin: Plugin) {
        reload(plugin)
    }

}
