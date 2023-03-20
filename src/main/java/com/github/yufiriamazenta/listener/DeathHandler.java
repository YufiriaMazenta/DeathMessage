package com.github.yufiriamazenta.listener;

import com.github.yufiriamazenta.DeathMessage;
import com.github.yufiriamazenta.data.DataContainer;
import com.github.yufiriamazenta.util.LangUtil;
import com.github.yufiriamazenta.util.NmsUtil;
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
    public void replaceMessage(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        String deathCause;
        EntityPlayer entityPlayer = null;
        try {
            entityPlayer = (EntityPlayer) entityField.get(deadPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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
                message = (TranslatableContents) getComponentContentsMethod.invoke(baseComponent);//get combatTracker then get deathMessage(ChatMessage)
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
        List<Object> objList = new ArrayList<>();
        BaseComponent name = new ComponentBuilder(deadPlayer.getDisplayName())
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(deadPlayer.getUniqueId().toString())))
                .getCurrentComponent();
        objList.add(name);
        if (objArrNum >= 2) {
            if (deadPlayer.getKiller() != null) {
                objList.add(new TextComponent(deadPlayer.getKiller().getDisplayName()));
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
                            String key = "entity.minecraft." + lastEntity.getType().name().toLowerCase(Locale.ROOT);
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
            String tag = NmsUtil.getItemTag(handItem);
            objList.add(new ComponentBuilder("[" + itemName + "]").event(
                    new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                            new Item(handItem.getType().getKey().toString(), handItem.getAmount(), ItemTag.ofNbt(tag)))).getCurrentComponent());
        }

        TranslatableComponent component = new TranslatableComponent(LangUtil.color(message), objList.toArray(new Object[0]));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(component);
        }
        entityHurtPlayerMap.remove(deadPlayer.getUniqueId());
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onPlayerHurtByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            Entity entity = event.getDamager();
            if (entity instanceof Projectile) {
                if (((Projectile) entity).getShooter() instanceof Mob mob) {
                    entity = mob;
                }
            }
            entityHurtPlayerMap.put(player.getUniqueId(), entity.getUniqueId());
        }
    }

}
