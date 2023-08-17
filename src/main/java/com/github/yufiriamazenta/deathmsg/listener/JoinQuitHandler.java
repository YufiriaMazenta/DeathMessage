package com.github.yufiriamazenta.deathmsg.listener;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import com.github.yufiriamazenta.deathmsg.data.DataContainer;
import com.github.yufiriamazenta.deathmsg.util.LangUtil;
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
