package pers.yufiria.deathmsg

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.util.PlayerUtil.isPlayerDeathMsgFilterOn

object PlaceholderAPIHooker: PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return "deathmessage"
    }

    override fun getAuthor(): String {
        return "Yufiria_"
    }

    override fun getVersion(): String {
        return DEATH_MESSAGE.description.version
    }

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        return if (player == null) {
            onPlaceholderRequest(null, params)
        } else{
            onPlaceholderRequest(player.player, params)
        }
    }

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        when (params) {
            "filter_state" -> {
                return if (player?.isPlayerDeathMsgFilterOn()?:return null) {
                    Configs.placeholderDeathMessageFilterOn.value()
                } else {
                    Configs.placeholderDeathMessageFilterOff.value()
                }
            }
            else -> {
                return null
            }
        }
    }

}