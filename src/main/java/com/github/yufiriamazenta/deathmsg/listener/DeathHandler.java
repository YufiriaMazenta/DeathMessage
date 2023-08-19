package com.github.yufiriamazenta.deathmsg.listener;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.commands.FilterDeathMessageCmd;
import com.github.yufiriamazenta.deathmsg.data.DataContainer;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
import com.github.yufiriamazenta.deathmsg.util.NmsUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
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

public enum DeathHandler implements Listener {

    INSTANCE;

    private Field entityField, deathCauseKeyField, combatTrackerField;
    private final Map<UUID, UUID> entityHurtPlayerMap;
    private Method toChatMethod, getComponentContentsMethod, getObjsMethod;

    DeathHandler() {
        try {
            Class<?> classCraftEntity = Class.forName("org.bukkit.craftbukkit." + DeathMessage.version + ".entity.CraftEntity");
            entityField = classCraftEntity.getDeclaredField("entity");
            entityField.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        entityHurtPlayerMap = new ConcurrentHashMap<>();
    }

    @EventHandler
    @SuppressWarnings("removal")
    public void onPlayerDeathReplaceMessage(PlayerDeathEvent event) {
        //以下获取死亡玩家的nms对象
        Player deadPlayer = event.getEntity();
        String deathCause;
        EntityPlayer entityPlayer = null;
        try {
            entityPlayer = (EntityPlayer) entityField.get(deadPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //以下对需要的反射内容进行获取并获取死亡消息的格式
        int objArrNum = 1;
        if (entityPlayer != null) {
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

            TranslatableContents message = null;
            try {
                message = (TranslatableContents) getComponentContentsMethod.invoke(baseComponent);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

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
                deathCause = (String) deathCauseKeyField.get(message);
            } catch (IllegalAccessException e) {
                deathCause = "death.attack.generic";
                e.printStackTrace();
            }

            if (getObjsMethod == null) {
                for (Method method : message.getClass().getMethods()) {
                    if (method.getReturnType().equals(Object[].class)) {
                        getObjsMethod = method;
                        break;
                    }
                }
            }
            try {
                Object[] objects = (Object[]) getObjsMethod.invoke(message);
                objArrNum = objects.length;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            return;
        }

        String message = DataContainer.getMessage(deathCause);
        if (message == null) {
            LangUtil.msg(Bukkit.getConsoleSender(), "Death Cause " + deathCause + " is Missing");
            deathCause = deathCause.replace(".", "_");
            DeathMessage.getInstance().getConfig().set(deathCause, List.of(deathCause));
            return;
        }

        //以下对死亡消息进行处理
        List<Object> objList = new ArrayList<>();
        //以下处理第一个对象的名字，一般是被杀的玩家
        String displayNameFormat = DeathMessage.INSTANCE.getConfig().getString("player_name_format", "%player_displayname%");
        String displayName = LangUtil.color(PlaceholderAPI.setPlaceholders(deadPlayer, displayNameFormat));
        BaseComponent name = new TextComponent(displayName);
        objList.add(name);
        if (objArrNum >= 2) {
            if (deadPlayer.getKiller() != null) {
                String killerDisplayName = LangUtil.color(PlaceholderAPI.setPlaceholders(deadPlayer.getKiller(), displayNameFormat));
                objList.add(new TextComponent(LangUtil.color(killerDisplayName)));
            } else {
                UUID lastEntityUuid = entityHurtPlayerMap.get(deadPlayer.getUniqueId());
                if (lastEntityUuid == null) {
                    String bedRespawnPoint = DataContainer.getMessage("bedRespawnPoint");
                    objList.add(new ComponentBuilder(LangUtil.color(bedRespawnPoint)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("MCPE-28723"))).getCurrentComponent());
                } else {
                    Entity lastEntity = Bukkit.getEntity(entityHurtPlayerMap.get(deadPlayer.getUniqueId()));
                    if (lastEntity == null) {
                        String bedRespawnPoint = DataContainer.getMessage("bedRespawnPoint");
                        objList.add(new ComponentBuilder(LangUtil.color(bedRespawnPoint)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("MCPE-28723"))).getCurrentComponent());
                    } else {
                        if (lastEntity.getCustomName() != null) {
                            objList.add(new ComponentBuilder(lastEntity.getCustomName()).getCurrentComponent());
                        } else {
                            String key = lastEntity.getType().getTranslationKey();
                            TranslatableComponent component = new TranslatableComponent(key);
                            objList.add(component);
                        }
                    }
                }
            }
        }
        if (objArrNum >= 3) {
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
            if (handItem.getEnchantments().size() < 1) {
                itemNameFormat = DeathMessage.getInstance().getConfig().getString("item_default_format", "&r%item_name%");
            } else {
                itemNameFormat = DeathMessage.getInstance().getConfig().getString("item_enchanted_format", "&r%item_name%");
            }
            itemName = itemNameFormat.replace("%item_name%", "[" + itemName + "]");
            String tag = NmsUtil.getItemTag(handItem);
            objList.add(new ComponentBuilder(LangUtil.color(itemName)).event(
                    new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                            new Item(handItem.getType().getKey().toString(), handItem.getAmount(), ItemTag.ofNbt(tag)))).getCurrentComponent());
        }

        
        TranslatableComponent component = new TranslatableComponent(LangUtil.color(message), objList.toArray(new Object[0]));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!deadPlayer.equals(onlinePlayer)) {
                if (filterDeathMsg(onlinePlayer))
                    continue;
            }
            onlinePlayer.spigot().sendMessage(component);
        }
        Bukkit.getConsoleSender().spigot().sendMessage(component);
        entityHurtPlayerMap.remove(deadPlayer.getUniqueId());
        event.setDeathMessage(null);
    }

    private boolean filterDeathMsg(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        Byte val = dataContainer.get(FilterDeathMessageCmd.INSTANCE.getFilterKey(), PersistentDataType.BYTE);
        return val != null && val != 0;
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
