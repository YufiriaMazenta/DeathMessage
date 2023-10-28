package com.github.yufiriamazenta.deathmsg.listener;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.data.DataContainer;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
import crypticlib.util.MsgUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public enum JoinQuitHandler implements Listener {

    INSTANCE;

    @EventHandler
    public void welcomeNewPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String welcomeMessage = DataContainer.getMessage("first_join_msg");
        if (!player.hasPlayedBefore()) {
            welcomeMessage = LangUtil.color(LangUtil.placeholder(player, welcomeMessage));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(welcomeMessage);
            }
        }
        String joinMessageString = DataContainer.getMessage("join_msg");
        if (joinMessageString != null && !joinMessageString.isEmpty()) {
            joinMessageString = LangUtil.color(LangUtil.placeholder(player, joinMessageString));
            event.setJoinMessage(null);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(joinMessageString);
            }
        }
    }

    @EventHandler
    public void changeQuitMessage(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitMessage = DataContainer.getMessage("quit_msg");
        if (quitMessage != null && !quitMessage.isEmpty()) {
            quitMessage = LangUtil.color(LangUtil.placeholder(player, quitMessage));
            event.setQuitMessage(quitMessage);
        }
    }
}
