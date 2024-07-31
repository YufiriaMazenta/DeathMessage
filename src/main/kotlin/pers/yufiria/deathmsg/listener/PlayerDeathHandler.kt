package pers.yufiria.deathmsg.listener

import crypticlib.chat.BukkitMsgSender
import crypticlib.chat.BukkitTextProcessor
import crypticlib.listener.EventListener
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.server.level.EntityPlayer
import net.minecraft.world.damagesource.CombatTracker
import org.bukkit.Bukkit
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import pers.yufiria.deathmsg.config.DeathMessages
import pers.yufiria.deathmsg.config.Configs
import pers.yufiria.deathmsg.event.DeathMessageSendEvent
import pers.yufiria.deathmsg.sync.DataSender
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@EventListener
object PlayerDeathHandler: Listener {

    private var deathCauseKeyField: Field? = null
    private var combatTrackerField: Field? = null
    private val entityHurtPlayerMap: MutableMap<UUID, UUID> = ConcurrentHashMap()
    private var entityGetHandleMethod: Method? = null
    private var toChatMethod: Method? = null
    private var getComponentContentsMethod: Method? = null
    private var getObjsMethod: Method? = null
    private var legacySerializer = LegacyComponentSerializer.legacy('&')

    @EventHandler
    fun onPlayerDeathReplaceMessage(event: PlayerDeathEvent) {
        //以下获取死亡玩家的nms对象
        val deadPlayer = event.entity
        val deathCause: String
        val entityPlayer: EntityPlayer = try {
            if (entityGetHandleMethod == null)
                entityGetHandleMethod = deadPlayer.javaClass.getMethod("getHandle")
            entityGetHandleMethod!!.invoke(deadPlayer) as EntityPlayer
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

        //以下对需要的反射内容进行获取并获取死亡消息的格式
        val objArrLength: Int
        val nmsDeathMsg = getNmsDeathMsg(entityPlayer)
        deathCause = getNmsDeathCause(nmsDeathMsg)
        objArrLength = getMsgObjsLength(nmsDeathMsg)
        if (!DeathMessages.hasDeathCause(deathCause)) {
            BukkitMsgSender.INSTANCE.sendMsg(Bukkit.getConsoleSender(), "Death Cause $deathCause is Missing")
            DeathMessages.addDeathMessage(deathCause, mutableListOf(deathCause))
            return
        }
        val message = DeathMessages.getMessage(deadPlayer, deathCause)
        if (message == null) {
            event.deathMessage = null
            return
        }

        //以下对死亡消息进行处理
        //此为消息中要替换的聊天组件
        val objList: MutableList<ComponentLike> = ArrayList()

        //以下处理第一个对象的名字，一般是被杀的玩家
        objList.add(getDeadPlayerComponent(deadPlayer))

        //当对象数量大于2等于2时,意味着有击杀者
        if (objArrLength >= 2) {
            objList.add(getKillerComponent(deadPlayer))
        }

        //当有三个以上对象时,说明有使用的武器
        if (objArrLength >= 3) {
            objList.add(getKillItemComponent(deadPlayer))
        }

        //组装成完整的死亡消息组件
        val deathMsgComponent = Component.translatable(BukkitTextProcessor.color(message), *objList.toTypedArray())
        entityHurtPlayerMap.remove(deadPlayer.uniqueId)
        val sendEvent = DeathMessageSendEvent(deathMsgComponent, deadPlayer)
        if (sendEvent.callEvent()) {
            DataSender.sendDeathMessage(sendEvent.deathMessage())
            event.deathMessage = null
        }
    }

    private fun getNmsDeathMsg(entityPlayer: EntityPlayer): TranslatableContents? {
        var tracker: CombatTracker? = null
        if (combatTrackerField == null) {
            for (field in entityPlayer.javaClass.superclass.superclass.getFields()) {
                if (field.type == CombatTracker::class.java) {
                    combatTrackerField = field
                    break
                }
            }
        }
        try {
            tracker = combatTrackerField!![entityPlayer] as CombatTracker
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        if (toChatMethod == null) {
            for (method in CombatTracker::class.java.getMethods()) {
                if (method.returnType == IChatBaseComponent::class.java) {
                    toChatMethod = method
                    break
                }
            }
        }
        var baseComponent: IChatBaseComponent? = null
        try {
            baseComponent = toChatMethod!!.invoke(tracker) as IChatBaseComponent
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        if (getComponentContentsMethod == null) {
            for (method in IChatBaseComponent::class.java.getMethods()) {
                if (method.returnType == ComponentContents::class.java) {
                    getComponentContentsMethod = method
                    break
                }
            }
        }
        return try {
            getComponentContentsMethod!!.invoke(baseComponent) as TranslatableContents
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            null
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            null
        }
    }

    private fun getNmsDeathCause(nmsDeathMsg: TranslatableContents?): String {
        if (deathCauseKeyField == null) {
            for (field in TranslatableContents::class.java.getDeclaredFields()) {
                if (field.type == String::class.java) {
                    deathCauseKeyField = field
                    break
                }
            }
        }
        deathCauseKeyField!!.setAccessible(true)
        return try {
            deathCauseKeyField!![nmsDeathMsg] as String
        } catch (e: IllegalAccessException) {
            "death.attack.generic"
        }
    }

    private fun getMsgObjsLength(nmsDeathMsg: TranslatableContents?): Int {
        if (getObjsMethod == null) {
            for (method in nmsDeathMsg!!.javaClass.getMethods()) {
                if (method.returnType == Array<Any>::class.java) {
                    getObjsMethod = method
                    break
                }
            }
        }
        return try {
            val objects = getObjsMethod!!.invoke(nmsDeathMsg) as Array<*>
            objects.size
        } catch (e: IllegalAccessException) {
            1
        } catch (e: InvocationTargetException) {
            1
        }
    }

    private fun getDeadPlayerComponent(deadPlayer: Player): Component {
        var deadPlayerDisplayName =
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                BukkitTextProcessor.color(PlaceholderAPI.setPlaceholders(deadPlayer, Configs.playerNameFormat.value()))
            else
                deadPlayer.displayName
        deadPlayerDisplayName = BukkitTextProcessor.color(deadPlayerDisplayName)
        val deserialize = legacySerializer.deserialize(deadPlayerDisplayName)
        deserialize.hoverEvent(deadPlayer.asHoverEvent())
        return deserialize
    }

    private fun getKillerComponent(deadPlayer: Player): Component {
        val killer = deadPlayer.killer
        return if (killer != null) {
            //当玩家存在击杀者时,返回击杀者的名字
            val displayName = killer.displayName()
            displayName.hoverEvent(killer.asHoverEvent())
            displayName
        } else {
            //当不存在击杀者时,尝试获取击杀实体的名字
            val lastEntityUuid = entityHurtPlayerMap[deadPlayer.uniqueId]
            if (lastEntityUuid == null || Bukkit.getEntity(lastEntityUuid) == null) {
                //当不存在击杀实体时,说明玩家可能死于方块爆炸
                val bedRespawnPoint = DeathMessages.getMessage(deadPlayer, "bad.respawn.point")?: "[刻意的游戏设计]"
                legacySerializer.deserialize(bedRespawnPoint)
            } else {
                //当存在击杀实体时,尝试获取击杀实体
                val lastEntity = Bukkit.getEntity(entityHurtPlayerMap[deadPlayer.uniqueId]!!)
                if (lastEntity!!.customName() != null) {
                    val customName = lastEntity.customName()!!
                    customName.hoverEvent(lastEntity.asHoverEvent())
                    customName
                } else {
                    val key = lastEntity.type.translationKey()
                    val translatable = Component.translatable(key)
                    translatable.hoverEvent(lastEntity.asHoverEvent())
                    translatable
                }
            }
        }
    }

    private fun getKillItemComponent(deadPlayer: Player): Component {
        val handItem: ItemStack = if (deadPlayer.killer != null) {
            deadPlayer.killer!!.inventory.itemInMainHand
        } else {
            val lastEntity = Bukkit.getEntity(entityHurtPlayerMap[deadPlayer.uniqueId]!!)
            (lastEntity as LivingEntity?)!!.equipment!!.itemInMainHand
        }
        val displayName = handItem.displayName()
        displayName.hoverEvent(handItem.asHoverEvent())
        return displayName
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
