package pers.yufiria.deathmsg.listener

import crypticlib.BukkitInvoker
import crypticlib.chat.BukkitMsgSender
import crypticlib.chat.BukkitTextProcessor
import crypticlib.listener.EventListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import pers.yufiria.deathmsg.config.DeathMessagesConfig
import pers.yufiria.deathmsg.event.DeathMessageSendEvent
import pers.yufiria.deathmsg.sync.DataSender
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@EventListener
object PlayerDeathHandler: Listener {

    private val entityHurtPlayerMap: MutableMap<UUID, UUID> = ConcurrentHashMap()
    private var legacySerializer = LegacyComponentSerializer.builder().hexColors().hexCharacter('#').character('&').build()

    @EventHandler
    fun onPlayerDeathReplaceMessage(event: PlayerDeathEvent) {
        val originDeathMessage = event.deathMessage()?:return

        if (originDeathMessage !is TranslatableComponent) {
            return
        }


        val translatableDeathMsgKey = originDeathMessage.key()
        //以下获取死亡玩家的nms对象
        val deadPlayer = event.entity

        if (!DeathMessagesConfig.hasDeathCause(translatableDeathMsgKey)) {
            BukkitMsgSender.INSTANCE.sendMsg(
                BukkitInvoker.byCommandSender(Bukkit.getConsoleSender()),
                "Death Cause $translatableDeathMsgKey is Missing"
            )
            DeathMessagesConfig.addDeathMessage(translatableDeathMsgKey, mutableListOf(translatableDeathMsgKey))
            return
        }
        var message = DeathMessagesConfig.getMessage(deadPlayer, translatableDeathMsgKey)
        if (message == null) {
            event.deathMessage = null
            return
        }

        //组装成完整的死亡消息组件
        val originDeathMsgArguments = originDeathMessage.arguments()

        message = BukkitTextProcessor.placeholder(deadPlayer, message);
        var deathMsgComponent: Component = legacySerializer.deserialize(message)
        deathMsgComponent = deathMsgComponent
            .replaceText(
                TextReplacementConfig
                    .builder()
                    .matchLiteral("%dead_player%")
                    .replacement(originDeathMsgArguments[0])
                    .build()
            )
        if (originDeathMsgArguments.size >= 2) {
            deathMsgComponent = deathMsgComponent
                .replaceText(
                TextReplacementConfig
                    .builder()
                    .matchLiteral("%killer%")
                    .replacement(originDeathMsgArguments[1])
                    .build()
                )
        }
        if (originDeathMsgArguments.size >= 3) {
            deathMsgComponent = deathMsgComponent
                .replaceText(
                    TextReplacementConfig
                        .builder()
                        .matchLiteral("%kill_item%")
                        .replacement(originDeathMsgArguments[2])
                        .build()
                )
        }
        entityHurtPlayerMap.remove(deadPlayer.uniqueId)
        val sendEvent = DeathMessageSendEvent(deathMsgComponent, deadPlayer)
        if (sendEvent.callEvent()) {
            DataSender.sendDeathMessage(sendEvent.deathMessage())
            event.deathMessage = null
        }
    }

    @EventHandler
    fun onPlayerHurtByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            var entity = event.damager
            if (entity is Projectile) {
                if (entity.shooter is Mob) {
                    entity = entity.shooter as Mob
                }
            } else if (entity is AreaEffectCloud) {
                if (entity.source is Entity) {
                    entity = entity.source as Mob
                }
            } else if (entity is EvokerFangs) {
                if (entity.owner != null) entity = entity.owner!!
            }
            entityHurtPlayerMap[event.entity.uniqueId] = entity.uniqueId
        }
    }
}
