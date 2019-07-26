package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    LifeMc2 plugin;

    public PlayerDeathListener(LifeMc2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        System.out.println("dead");
        Player killer = event.getEntity().getKiller();
        if (killer != null){
            int lives = plugin.getDataHandler().getLives(killer);
            plugin.getDataHandler().setLives(killer, lives + 1);
            killer.sendMessage(Lang.GAINED_A_LIFE.getConfigValue());
        }
    }

}
