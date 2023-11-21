package com.github.yufiriamazenta.deathmsg.listener

import com.github.yufiriamazenta.deathmsg.DEATH_MESSAGE
import com.github.yufiriamazenta.deathmsg.data.DataManager
import com.github.yufiriamazenta.deathmsg.util.NmsUtil.getItemTagJson
import crypticlib.CrypticLib
import crypticlib.listener.BukkitListener
import crypticlib.util.MsgUtil
import crypticlib.util.TextUtil
import me.clip.placeholderapi.PlaceholderAPI
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.*
import net.md_5.bungee.api.chat.hover.content.Item
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
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@BukkitListener
class DeathHandler: Listener {

    private var entityField: Field? = null
    private var deathCauseKeyField: Field? = null
    private var combatTrackerField: Field? = null
    private val entityHurtPlayerMap: MutableMap<UUID, UUID>
    private var toChatMethod: Method? = null
    private var getComponentContentsMethod: Method? = null
    private var getObjsMethod: Method? = null

    init {
        try {
            val classCraftEntity =
                Class.forName("org.bukkit.craftbukkit." + CrypticLib.nmsVersion() + ".entity.CraftEntity")
            entityField = classCraftEntity.getDeclaredField("entity")
            entityField!!.setAccessible(true)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        entityHurtPlayerMap = ConcurrentHashMap()
    }

    @EventHandler
    fun onPlayerDeathReplaceMessage(event: PlayerDeathEvent) {
        //以下获取死亡玩家的nms对象
        val deadPlayer = event.entity
        var deathCause: String
        val entityPlayer: EntityPlayer = try {
            entityField!![deadPlayer] as EntityPlayer
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

        //以下对需要的反射内容进行获取并获取死亡消息的格式
        val objArrNum: Int
        val nmsDeathMsg = getNmsDeathMsg(entityPlayer)
        deathCause = getNmsDeathCause(nmsDeathMsg)
        objArrNum = getMsgObjsLength(nmsDeathMsg)
        val message = DataManager.getMessage(deadPlayer, deathCause)
        if (message == null) {
            MsgUtil.sendMsg(Bukkit.getConsoleSender(), "Death Cause $deathCause is Missing")
            deathCause = deathCause.replace(".", "_")
            DEATH_MESSAGE.getConfig().set(deathCause, deathCause)
            return
        }

        //以下对死亡消息进行处理
        //此为消息中要替换的聊天组件
        val objList: MutableList<BaseComponent> = ArrayList()

        //以下处理第一个对象的名字，一般是被杀的玩家
        val displayNameFormat: String = DEATH_MESSAGE.config.getString("player_name_format", "%player_displayname%")!!
        objList.add(getDeadPlayerComponent(deadPlayer, displayNameFormat))

        //当对象数量大于2等于2时,意味着有击杀者
        if (objArrNum >= 2) {
            objList.add(getKillerComponent(deadPlayer, displayNameFormat))
        }

        //当有三个以上对象时,说明有使用的武器
        if (objArrNum >= 3) {
            objList.add(getKillItemComponent(deadPlayer))
        }

        //组装成完整的死亡消息组件
        val deathMsgComponent = TranslatableComponent(TextUtil.color(message), *objList.toTypedArray())

        val chatMessageTypeStr = DEATH_MESSAGE.config.getString("death_message_type", "chat")!!.uppercase()
        val chatMessageType = try {
            ChatMessageType.valueOf(chatMessageTypeStr)
        } catch (e: Exception) {
            ChatMessageType.CHAT
        }

        //发送死亡消息给没有屏蔽死亡消息的玩家
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (deadPlayer != onlinePlayer) {
                if (DEATH_MESSAGE.isPlayerDeathMsgFilterOn(onlinePlayer)) continue
            }
            if (chatMessageTypeStr != "ALL")
                onlinePlayer.spigot().sendMessage(chatMessageType, deathMsgComponent)
            else {
                onlinePlayer.spigot().sendMessage(ChatMessageType.CHAT, deathMsgComponent)
                onlinePlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, deathMsgComponent)
            }
        }
        Bukkit.getConsoleSender().spigot().sendMessage(deathMsgComponent)
        entityHurtPlayerMap.remove(deadPlayer.uniqueId)
        event.deathMessage = null
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
            e.printStackTrace()
            1
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            1
        }
    }

    private fun getDeadPlayerComponent(deadPlayer: Player, displayNameFormat: String): BaseComponent {
        val deadPlayerDisplayName: String = if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) TextUtil.color(
            PlaceholderAPI.setPlaceholders(deadPlayer, displayNameFormat)
        ) else TextUtil.color(deadPlayer.displayName)
        val deadPlayerDisplayCompound: BaseComponent = TextComponent()
        for (baseComponent in TextComponent.fromLegacyText(deadPlayerDisplayName)) {
            deadPlayerDisplayCompound.addExtra(baseComponent)
        }
        return deadPlayerDisplayCompound
    }

    private fun getKillerComponent(deadPlayer: Player, displayNameFormat: String): BaseComponent {
        return if (deadPlayer.killer != null) {
            //当玩家存在击杀者时,返回击杀者的名字
            val killerDisplayNameStr: String = if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) TextUtil.color(
                PlaceholderAPI.setPlaceholders(deadPlayer.killer, displayNameFormat)
            ) else TextUtil.color(
                deadPlayer.killer!!.displayName
            )
            val killerDisplayCompound: BaseComponent = TextComponent()
            for (baseComponent in TextComponent.fromLegacyText(killerDisplayNameStr)) {
                killerDisplayCompound.addExtra(baseComponent)
            }
            killerDisplayCompound
        } else {
            //当不存在击杀者时,尝试获取击杀实体的名字
            val lastEntityUuid = entityHurtPlayerMap[deadPlayer.uniqueId]
            if (lastEntityUuid == null || Bukkit.getEntity(lastEntityUuid) == null) {
                //当不存在击杀实体时,说明玩家可能死于方块爆炸
                val bedRespawnPoint = DataManager.getMessage(deadPlayer, "bed_respawn_point")
                val bedRespawnDisplayCompound: BaseComponent = TextComponent()
                for (baseComponent in TextComponent.fromLegacyText(bedRespawnPoint)) {
                    bedRespawnDisplayCompound.addExtra(baseComponent)
                }
                bedRespawnDisplayCompound
            } else {
                //当存在击杀实体时,尝试获取击杀实体
                val lastEntity = Bukkit.getEntity(entityHurtPlayerMap[deadPlayer.uniqueId]!!)
                if (lastEntity!!.customName != null) {
                    val customNameDisplayCompound: BaseComponent = TextComponent()
                    for (baseComponent in TextComponent.fromLegacyText(
                        lastEntity.customName
                    )) {
                        customNameDisplayCompound.addExtra(baseComponent)
                    }
                    customNameDisplayCompound
                } else {
                    val key = lastEntity.type.translationKey
                    TranslatableComponent(key)
                }
            }
        }
    }

    private fun getKillItemComponent(deadPlayer: Player): BaseComponent {
        var itemName: String
        val handItem: ItemStack = if (deadPlayer.killer != null) {
            deadPlayer.killer!!.inventory.itemInMainHand
        } else {
            val lastEntity = Bukkit.getEntity(entityHurtPlayerMap[deadPlayer.uniqueId]!!)
            (lastEntity as LivingEntity?)!!.equipment!!.itemInMainHand
        }
        val meta = handItem.itemMeta
        itemName = if (meta == null) handItem.type.name else {
            if (meta.hasDisplayName()) {
                meta.displayName
            } else {
                meta.localizedName
            }
        }
        val itemNameFormat: String = if (handItem.enchantments.isEmpty()) {
            DEATH_MESSAGE.getConfig().getString("item_default_format", "&r%item_name%")!!
        } else {
            DEATH_MESSAGE.getConfig().getString("item_enchanted_format", "&r%item_name%")!!
        }
        itemName = itemNameFormat.replace("%item_name%", "[$itemName]")
        val tag = getItemTagJson(handItem)
        val itemDisplayCompound: BaseComponent = TextComponent()
        for (itemNameCompound in TextComponent.fromLegacyText(itemName)) {
            itemNameCompound.hoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_ITEM,
                Item(handItem.type.getKey().toString(), handItem.amount, ItemTag.ofNbt(tag))
            )
            itemDisplayCompound.addExtra(itemNameCompound)
        }
        return itemDisplayCompound
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
