package com.github.yufiriamazenta.deathmsg.listener;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.data.DataContainer;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public enum JoinQuitHandler implements Listener {

    INSTANCE;

    @EventHandler
    public void welcomeNewPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String welcomeMessage = DataContainer.getMessage("helloMessage");
        if (!player.hasPlayedBefore()) {
            welcomeMessage = welcomeMessage.replace("{}", player.getDisplayName());;
            welcomeMessage = LangUtil.color(welcomeMessage);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(welcomeMessage);
            }
        }
        String joinMessageString = DataContainer.getMessage("joinMessage");
        if (joinMessageString != null && !joinMessageString.equals("")) {
            joinMessageString = joinMessageString.replace("{}", player.getDisplayName());
            joinMessageString = LangUtil.color(joinMessageString);
            event.setJoinMessage(null);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(joinMessageString);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFirstPlay(PlayerJoinEvent event) {
        if (!DeathMessage.plugin.getConfig().getBoolean("new_player_tpr", true)) {
            return;
        }
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            try {
                player.getScheduler().execute(DeathMessage.plugin, () -> {
                    Bukkit.dispatchCommand(player, "tpr");
                    LangUtil.msg(player, "new_player_msg");
                }, null, 60L);
                Bukkit.getGlobalRegionScheduler().runDelayed(DeathMessage.plugin, task -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco set "+ player.getName() + " 2"), 60L);
                player.getScheduler().execute(DeathMessage.plugin, () -> {
                    player.setBedSpawnLocation(player.getLocation());
                    player.getInventory().addItem(new ItemStack(Material.PINK_BED));
                }, null,80L);
            } catch (Exception e) {
                Bukkit.getScheduler().runTaskLater(DeathMessage.plugin, () -> {
                    Bukkit.dispatchCommand(player, "tpr");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco set "+ player.getName() + " 2");
                    LangUtil.msg(player, "new_player_msg");
                }, 60L);
                Bukkit.getScheduler().runTaskLater(DeathMessage.plugin, () -> {
                    player.getInventory().addItem(new ItemStack(Material.PINK_BED));
                    player.setBedSpawnLocation(player.getLocation());
                }, 80L);
            }
        }
    }

    @EventHandler
    public void changeQuitMessage(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitMessage = DataContainer.getMessage("quitMessage");
        if (quitMessage != null && !quitMessage.equals("")) {
            quitMessage = quitMessage.replace("{}", player.getDisplayName());
            quitMessage = LangUtil.color(quitMessage);
            event.setQuitMessage(quitMessage);
        }
    }
}
