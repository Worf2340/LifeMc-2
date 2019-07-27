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

        // Lose life on death
        final Player player = event.getEntity();

        if (!player.hasPermission("lifemc.lives.lose"))
            return;

        int lives = plugin.getDataHandler().getLives(player);

        if (lives <= 1) {

            plugin.getDataHandler().setLives(player, 0);

            // Kick player after .5 seconds
            plugin.getServer().getScheduler()
                    .runTaskLater(plugin, new Runnable() {
                        public void run() {
                            player.kickPlayer(Lang.KICK_OUT_OF_LIVES.getConfigValue());
                        }
                    }, 10L);
        } else {

            plugin.getDataHandler().setLives(player, lives - 1);
            player.sendMessage(Lang.LOST_A_LIFE.getConfigValue());
        }

        // Gain life on murder
        if (!(plugin.getConfigHandler().gainLifeAtMurder())){
            return;
        }

        Player killer = event.getEntity().getKiller();
        if (killer != null){
            lives = plugin.getDataHandler().getLives(killer);
            plugin.getDataHandler().setLives(killer, lives + 1);
            killer.sendMessage(Lang.GAINED_A_LIFE.getConfigValue());
        }
    }

}
