package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    LifeMc2 plugin;

    public PlayerQuitListener(LifeMc2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getDataHandler().savePlayer(event.getPlayer());

    }
}
