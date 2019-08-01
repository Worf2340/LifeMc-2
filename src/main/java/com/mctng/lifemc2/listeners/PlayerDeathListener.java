package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class PlayerDeathListener implements Listener {

    LifeMc2 plugin;

    public PlayerDeathListener(LifeMc2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){

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


        // Chance of gaining a life on murder
        int chance = plugin.getConfigHandler().gainLifeOnMurderChance();
        Random rand = new Random();
        int randInt = rand.nextInt(100);
       // System.out.println(randInt);
        boolean gainLife = randInt < chance;

        if (gainLife) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                lives = plugin.getDataHandler().getLives(killer);
                plugin.getDataHandler().setLives(killer, lives + 1);
                killer.sendMessage(Lang.GAINED_A_LIFE.getConfigValue());
            }
        }
    }

}
