package com.github.yufiriamazenta.listener;

import com.github.yufiriamazenta.DeathMessage;
import com.github.yufiriamazenta.data.DataContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public enum JoinQuitHandler implements Listener {

    INSTANCE;

    @EventHandler
    public void welcomeNewPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String welcomeMessage = DataContainer.getMessage("helloMessage");
        if (!player.hasPlayedBefore()) {
            welcomeMessage = DeathMessage.color(welcomeMessage);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(welcomeMessage);
            }
        }
        String joinMessageString = DataContainer.getMessage("joinMessage");
        if (joinMessageString != null && !joinMessageString.equals("")) {
            joinMessageString = joinMessageString.replace("{}", player.getDisplayName());
            joinMessageString = DeathMessage.color(joinMessageString);
            event.setJoinMessage(null);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(joinMessageString);
            }
        }
    }

    @EventHandler
    public void changeQuitMessage(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitMessage = DataContainer.getMessage("quitMessage");
        if (quitMessage != null && !quitMessage.equals("")) {
            quitMessage = quitMessage.replace("{}", player.getDisplayName());
            quitMessage = DeathMessage.color(quitMessage);
            event.setQuitMessage(quitMessage);
        }
    }
}
