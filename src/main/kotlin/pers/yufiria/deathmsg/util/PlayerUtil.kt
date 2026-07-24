package pers.yufiria.deathmsg.util

import crypticlib.BukkitPlayer
import crypticlib.chat.BukkitMsgSender
import crypticlib.lifecycle.LifeCycle
import crypticlib.lifecycle.LifeCycleTask
import crypticlib.lifecycle.LifeCycleTaskSettings
import crypticlib.lifecycle.TaskRule
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import pers.yufiria.deathmsg.DEATH_MESSAGE
import pers.yufiria.deathmsg.config.Configs

@LifeCycleTaskSettings(rules = [TaskRule(lifeCycle = LifeCycle.ENABLE)])
object PlayerUtil: LifeCycleTask {

    private lateinit var deathMessageFilterKey: NamespacedKey

    fun Player.toggleFilter() {
        val filterFlag = this.isPlayerDeathMsgFilterOn()
        if (filterFlag) {
            this.setFilterOff()
        } else {
            this.setFilterOn()
        }
    }

    fun Player.setFilterOn() {
        val dataContainer = this.persistentDataContainer
        dataContainer[deathMessageFilterKey, PersistentDataType.BYTE] = 1.toByte()
        BukkitMsgSender.INSTANCE.sendMsg(BukkitPlayer.byPlayer(player), Configs.pluginMessageFilterOn.value())
    }

    fun Player.setFilterOff() {
        val dataContainer = this.persistentDataContainer
        dataContainer[deathMessageFilterKey, PersistentDataType.BYTE] = 0.toByte()
        BukkitMsgSender.INSTANCE.sendMsg(BukkitPlayer.byPlayer(player), Configs.pluginMessageFilterOff.value())
    }


    fun Player.isPlayerDeathMsgFilterOn(): Boolean {
        val dataContainer = this.persistentDataContainer
        val filterFlag = dataContainer.get(deathMessageFilterKey, PersistentDataType.BYTE)
        return filterFlag != null && filterFlag.toInt() != 0
    }

    override fun lifecycle(p0: Any, p1: LifeCycle?) {
        deathMessageFilterKey = NamespacedKey(DEATH_MESSAGE, "death_msg_filter")
    }


}