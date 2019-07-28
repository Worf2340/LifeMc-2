package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.lang.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;

public class PlayerConsumeListener implements Listener {

    private LifeMc2 plugin;

    public PlayerConsumeListener(LifeMc2 lifemc) {
        plugin = lifemc;
    }

    @SuppressWarnings("Duplicates")
    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        if (!plugin.getConfigHandler().isEatingEnabled()) return;

        Player player = event.getPlayer();

        if (!player.hasPermission("lifemc.lives.gain")) return;

        List<Material> eatableItems = plugin.getConfigHandler().getEatableItems();

        // Not eatable
        if (!eatableItems.contains(event.getItem().getType())) return;

        int lives = plugin.getDataHandler().getLives(player);

        if (lives >= this.plugin.getDataHandler().getMaxLives(player.getName())) {
            event.setCancelled(true);
            player.sendMessage(Lang.CANNOT_OBTAIN_MORE_LIVES.getConfigValue());
            return;
        }

        // Add a life
        plugin.getDataHandler().setLives(player, lives + 1);

        player.sendMessage(Lang.GAINED_A_LIFE.getConfigValue());
    }
}
