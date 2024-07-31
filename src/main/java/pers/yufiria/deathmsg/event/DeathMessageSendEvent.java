package pers.yufiria.deathmsg.event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeathMessageSendEvent extends Event implements Cancellable {

    private Component deathMessage;
    private final Player deadPlayer;
    private boolean cancelled;

    public DeathMessageSendEvent(@NotNull Component deathMessage, @Nullable Player deadPlayer) {
        this.deathMessage = deathMessage;
        this.deadPlayer = deadPlayer;
    }

    public @NotNull Component deathMessage() {
        return deathMessage;
    }

    public DeathMessageSendEvent setDeathMessage(@NotNull Component deathMessage) {
        this.deathMessage = deathMessage;
        return this;
    }

    public @Nullable Player deadPlayer() {
        return deadPlayer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
