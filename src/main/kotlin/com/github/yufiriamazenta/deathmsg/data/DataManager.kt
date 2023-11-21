package com.github.yufiriamazenta.deathmsg.data

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import crypticlib.util.YamlConfigUtil
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object DataManager {

    private val deathMsgMap: MutableMap<String, List<*>>
    private var random: Random?

    init {
        deathMsgMap = ConcurrentHashMap()
        random = Random()
    }

    fun reloadData() {
        deathMsgMap.clear()
        DEATH_MESSAGE.reloadConfig()
        val config: FileConfiguration = DEATH_MESSAGE.getConfig()
        for (key in config.getConfigurationSection("death_message")!!.getKeys(false)) {
            deathMsgMap[key.replace("_", ".")] = YamlConfigUtil.configList2List(
                config.getList(
                    "death_message.$key", ArrayList<Any>()
                )
            )
        }
    }

    private fun hasDeathCause(deathCause: String): Boolean {
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
                return if (messageList.isEmpty()) null else messageList[random!!.nextInt(messageList.size)]
            } else if (list[0] is String) {
                return list[random!!.nextInt(list.size)] as String?
            }
        }
        return null
    }
}
