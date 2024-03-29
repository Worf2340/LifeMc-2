package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.lang.Lang;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

//Listener class

/*
This class no longer works properly with EssentialsX
 */
public class PlayerRespawnListener implements Listener {

	private LifeMc2 plugin;

	public PlayerRespawnListener(LifeMc2 instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player player = event.getPlayer();

		if (!player.hasPermission("lifemc.lives.lose"))
			return;

		int lives = plugin.getDataHandler().getLives(player);

		if (lives <= 1) {

			plugin.getDataHandler().setLives(player, 0);

			//player.getInventory().clear();
			//player.getEquipment().clear();
			//player.setExp(0);

			// Teleport player to bed so they spawn at their bed after they get unbanned.
			if (plugin.getConfigHandler().spawnAtBedAfterBan()) {
				final Location bedLocation = player.getBedSpawnLocation();

				if (bedLocation != null) {
					event.setRespawnLocation(bedLocation);
					
					// Teleport player after .25 seconds
					plugin.getServer().getScheduler()
							.runTaskLater(plugin, new Runnable() {
								public void run() {
									player.teleport(bedLocation);
								}
							}, 5L);
					
				}
			}

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
	}
}