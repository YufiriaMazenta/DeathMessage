package com.github.yufiriamazenta.deathmsg.listener;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.data.DataManager;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
import com.github.yufiriamazenta.deathmsg.util.NmsUtil;
import crypticlib.CrypticLib;
import crypticlib.annotations.BukkitListener;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.damagesource.CombatTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@BukkitListener
public class DeathHandler implements Listener {

    private Field entityField, deathCauseKeyField, combatTrackerField;
    private final Map<UUID, UUID> entityHurtPlayerMap;
    private Method toChatMethod, getComponentContentsMethod, getObjsMethod;

    DeathHandler() {
        try {
            Class<?> classCraftEntity = Class.forName("org.bukkit.craftbukkit." + CrypticLib.nmsVersion() + ".entity.CraftEntity");
            entityField = classCraftEntity.getDeclaredField("entity");
            entityField.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        entityHurtPlayerMap = new ConcurrentHashMap<>();
    }

    @EventHandler
    public void onPlayerDeathReplaceMessage(PlayerDeathEvent event) {
        //以下获取死亡玩家的nms对象
        Player deadPlayer = event.getEntity();
        String deathCause;
        EntityPlayer entityPlayer;
        try {
            entityPlayer = (EntityPlayer) entityField.get(deadPlayer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        //以下对需要的反射内容进行获取并获取死亡消息的格式
        int objArrNum;
        TranslatableContents nmsDeathMsg = getNmsDeathMsg(entityPlayer);
        deathCause = getNmsDeathCause(nmsDeathMsg);
        objArrNum = getMsgObjsLength(nmsDeathMsg);

        String message = DataManager.getMessage(deadPlayer, deathCause);
        if (message == null) {
            LangUtil.msg(Bukkit.getConsoleSender(), "Death Cause " + deathCause + " is Missing");
            deathCause = deathCause.replace(".", "_");
            DeathMessage.getInstance().getConfig().set(deathCause, List.of(deathCause));
            return;
        }

        //以下对死亡消息进行处理
        //此为消息中要替换的聊天组件
        List<BaseComponent> objList = new ArrayList<>();

        //以下处理第一个对象的名字，一般是被杀的玩家
        String displayNameFormat = DeathMessage.INSTANCE.getConfig().getString("player_name_format", "%player_displayname%");
        objList.add(getDeadPlayerComponent(deadPlayer, displayNameFormat));

        //当对象数量大于2等于2时,意味着有击杀者
        if (objArrNum >= 2) {
            objList.add(getKillerComponent(deadPlayer, displayNameFormat));
        }

        //当有三个以上对象时,说明有使用的武器
        if (objArrNum >= 3) {
            objList.add(getKillItemComponent(deadPlayer));
        }

        //组装成完整的死亡消息组件
        TranslatableComponent deathMsgComponent = new TranslatableComponent(LangUtil.color(message), objList.toArray());

        //发送死亡消息给没有屏蔽死亡消息的玩家
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!deadPlayer.equals(onlinePlayer)) {
                if (isPlayerDeathMsgFilterOn(onlinePlayer))
                    continue;
            }
            onlinePlayer.spigot().sendMessage(deathMsgComponent);
        }
        Bukkit.getConsoleSender().spigot().sendMessage(deathMsgComponent);
        entityHurtPlayerMap.remove(deadPlayer.getUniqueId());
        event.setDeathMessage(null);
    }

    private boolean isPlayerDeathMsgFilterOn(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        Byte val = dataContainer.get(DeathMessage.getInstance().getDeathMsgFilterKey(), PersistentDataType.BYTE);
        return val != null && val != 0;
    }

    private TranslatableContents getNmsDeathMsg(EntityPlayer entityPlayer) {
        CombatTracker tracker = null;
        if (combatTrackerField == null) {
            for (Field field : entityPlayer.getClass().getSuperclass().getSuperclass().getFields()) {
                if (field.getType().equals(CombatTracker.class)) {
                    combatTrackerField = field;
                    break;
                }
            }
        }
        try {
            tracker = (CombatTracker) combatTrackerField.get(entityPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (toChatMethod == null) {
            for (Method method : CombatTracker.class.getMethods()) {
                if (method.getReturnType().equals(IChatBaseComponent.class)) {
                    toChatMethod = method;
                    break;
                }
            }
        }

        IChatBaseComponent baseComponent = null;
        try {
            baseComponent = (IChatBaseComponent) toChatMethod.invoke(tracker);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (getComponentContentsMethod == null) {
            for (Method method : IChatBaseComponent.class.getMethods()) {
                if (method.getReturnType().equals(ComponentContents.class)) {
                    getComponentContentsMethod = method;
                    break;
                }
            }
        }

        try {
            return  (TranslatableContents) getComponentContentsMethod.invoke(baseComponent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getNmsDeathCause(TranslatableContents nmsDeathMsg) {
        if (deathCauseKeyField == null) {
            for (Field field : TranslatableContents.class.getDeclaredFields()) {
                if (field.getType().equals(String.class)) {
                    deathCauseKeyField = field;
                    break;
                }
            }
        }
        deathCauseKeyField.setAccessible(true);
        try {
            return (String) deathCauseKeyField.get(nmsDeathMsg);
        } catch (IllegalAccessException e) {
            return "death.attack.generic";
        }
    }

    private int getMsgObjsLength(TranslatableContents nmsDeathMsg) {
        if (getObjsMethod == null) {
            for (Method method : nmsDeathMsg.getClass().getMethods()) {
                if (method.getReturnType().equals(Object[].class)) {
                    getObjsMethod = method;
                    break;
                }
            }
        }
        try {
            Object[] objects = (Object[]) getObjsMethod.invoke(nmsDeathMsg);
            return objects.length;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private BaseComponent getDeadPlayerComponent(Player deadPlayer, String displayNameFormat) {
        String deadPlayerDisplayName;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            deadPlayerDisplayName = LangUtil.color(PlaceholderAPI.setPlaceholders(deadPlayer, displayNameFormat));
        else
            deadPlayerDisplayName = LangUtil.color(deadPlayer.getDisplayName());
        BaseComponent deadPlayerDisplayCompound = new TextComponent();
        for (BaseComponent baseComponent : TextComponent.fromLegacyText(deadPlayerDisplayName)) {
            deadPlayerDisplayCompound.addExtra(baseComponent);
        }
        return (deadPlayerDisplayCompound);
    }

    private BaseComponent getKillerComponent(Player deadPlayer, String displayNameFormat) {
        if (deadPlayer.getKiller() != null) {
            //当玩家存在击杀者时,返回击杀者的名字
            String killerDisplayNameStr;
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                killerDisplayNameStr = LangUtil.color(PlaceholderAPI.setPlaceholders(deadPlayer.getKiller(), displayNameFormat));
            else
                killerDisplayNameStr = LangUtil.color(deadPlayer.getKiller().getDisplayName());

            BaseComponent killerDisplayCompound = new TextComponent();
            for (BaseComponent baseComponent : TextComponent.fromLegacyText(killerDisplayNameStr)) {
                killerDisplayCompound.addExtra(baseComponent);
            }
            return killerDisplayCompound;
        } else {
            //当不存在击杀者时,尝试获取击杀实体的名字
            UUID lastEntityUuid = entityHurtPlayerMap.get(deadPlayer.getUniqueId());
            if (lastEntityUuid == null || Bukkit.getEntity(lastEntityUuid) == null) {
                //当不存在击杀实体时,说明玩家可能死于方块爆炸
                String bedRespawnPoint = DataManager.getMessage(deadPlayer, "bed_respawn_point");
                BaseComponent bedRespawnDisplayCompound = new TextComponent();
                for (BaseComponent baseComponent : TextComponent.fromLegacyText(bedRespawnPoint)) {
                    bedRespawnDisplayCompound.addExtra(baseComponent);
                }
                return bedRespawnDisplayCompound;
            } else {
                //当存在击杀实体时,尝试获取击杀实体
                Entity lastEntity = Bukkit.getEntity(entityHurtPlayerMap.get(deadPlayer.getUniqueId()));
                if (lastEntity.getCustomName() != null) {
                    BaseComponent customNameDisplayCompound = new TextComponent();
                    for (BaseComponent baseComponent : TextComponent.fromLegacyText(lastEntity.getCustomName())) {
                        customNameDisplayCompound.addExtra(baseComponent);
                    }
                    return customNameDisplayCompound;
                } else {
                    String key = lastEntity.getType().getTranslationKey();
                    return new TranslatableComponent(key);
                }
            }
        }
    }

    private BaseComponent getKillItemComponent(Player deadPlayer) {
        String itemName;
        ItemStack handItem;
        if (deadPlayer.getKiller() != null) {
            handItem = deadPlayer.getKiller().getInventory().getItemInMainHand();
        } else {
            Entity lastEntity = Bukkit.getEntity(entityHurtPlayerMap.get(deadPlayer.getUniqueId()));
            handItem = ((LivingEntity) lastEntity).getEquipment().getItemInMainHand();
        }
        ItemMeta meta = handItem.getItemMeta();
        if (meta == null)
            itemName = handItem.getType().name();
        else {
            if (meta.hasDisplayName()) {
                itemName = meta.getDisplayName();
            } else {
                itemName = meta.getLocalizedName();
            }
        }
        String itemNameFormat;
        if (handItem.getEnchantments().isEmpty()) {
            itemNameFormat = DeathMessage.getInstance().getConfig().getString("item_default_format", "&r%item_name%");
        } else {
            itemNameFormat = DeathMessage.getInstance().getConfig().getString("item_enchanted_format", "&r%item_name%");
        }

        itemName = itemNameFormat.replace("%item_name%", "[" + itemName + "]");
        String tag = NmsUtil.getItemTag(handItem);
        BaseComponent itemDisplayCompound = new TextComponent();
        for (BaseComponent itemNameCompound : TextComponent.fromLegacyText(itemName)) {
            itemNameCompound.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                    new Item(handItem.getType().getKey().toString(), handItem.getAmount(), ItemTag.ofNbt(tag))));
            itemDisplayCompound.addExtra(itemNameCompound);
        }
        return itemDisplayCompound;
    }

    @EventHandler
    public void onPlayerHurtByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            Entity entity = event.getDamager();
            if (entity instanceof Projectile) {
                if (((Projectile) entity).getShooter() instanceof Mob mob) {
                    entity = mob;
                }
            } else if (entity instanceof AreaEffectCloud) {
                if (((AreaEffectCloud) entity).getSource() instanceof Entity entity1) {
                    entity = entity1;
                }
            } else if (entity instanceof EvokerFangs) {
                if (((EvokerFangs) entity).getOwner() != null)
                    entity = ((EvokerFangs) entity).getOwner();
            }
            entityHurtPlayerMap.put(player.getUniqueId(), entity.getUniqueId());
        }
    }

}
