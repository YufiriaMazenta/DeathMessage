package pers.yufiria.deathmsg.util

import crypticlib.chat.BukkitMsgSender
import crypticlib.lifecycle.AutoTask
import crypticlib.lifecycle.BukkitLifeCycleTask
import crypticlib.lifecycle.LifeCycle
import crypticlib.lifecycle.TaskRule
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import pers.yufiria.deathmsg.DEATH_MESSAGE
import pers.yufiria.deathmsg.config.Configs

@AutoTask(rules = [TaskRule(lifeCycle = LifeCycle.ENABLE)])
object PlayerUtil: BukkitLifeCycleTask {

    private lateinit var deathMessageFilterKey: NamespacedKey

    fun toggleFilter(player: Player) {
        val filterFlag = isPlayerDeathMsgFilterOn(player)
        if (filterFlag) {
            setFilterOff(player)
        } else {
            setFilterOn(player)
        }
    }

    fun setFilterOn(player: Player) {
        val dataContainer = player.persistentDataContainer
        dataContainer[deathMessageFilterKey, PersistentDataType.BYTE] = 1.toByte()
        BukkitMsgSender.INSTANCE.sendMsg(player, Configs.pluginMessageFilterOn.value())
    }

    fun setFilterOff(player: Player) {
        val dataContainer = player.persistentDataContainer
        dataContainer[deathMessageFilterKey, PersistentDataType.BYTE] = 0.toByte()
        BukkitMsgSender.INSTANCE.sendMsg(player, Configs.pluginMessageFilterOff.value())
    }


    fun isPlayerDeathMsgFilterOn(player: Player): Boolean {
        val dataContainer = player.persistentDataContainer
        val filterFlag = dataContainer[deathMessageFilterKey, PersistentDataType.BYTE]
        return filterFlag != null && filterFlag.toInt() != 0
    }

    override fun run(p0: Plugin?, p1: LifeCycle?) {
        deathMessageFilterKey = NamespacedKey(DEATH_MESSAGE, "death_msg_filter")
    }


}