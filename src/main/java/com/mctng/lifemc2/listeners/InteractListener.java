package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.lang.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

//Listener class
public class InteractListener implements Listener {

	private LifeMc2 plugin;

	public InteractListener(LifeMc2 lifemc) {
		plugin = lifemc;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!plugin.getConfigHandler().isEatingEnabled()) return;
		
		if (!event.hasItem()) return;
		
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		// Players can eat food when they are sneaking.
		if (player.isSneaking()) return;
		
		if (!player.hasPermission("lifemc.lives.gain")) return;
		
		List<Material> eatableItems = plugin.getConfigHandler().getEatableItems();
		
		// Not eatable
		if (!eatableItems.contains(item.getType())) return;
		
		int lives = plugin.getDataHandler().getLives(player);
		
		if (lives >= this.plugin.getConfigHandler().getMaxLives(player.getName())) {
			player.sendMessage(Lang.CANNOT_OBTAIN_MORE_LIVES.getConfigValue());
			return;
		}
		
		if (item.getAmount() <= 1) {
			// Remove item
			player.setItemInHand(null);
		} else {
			item.setAmount(item.getAmount() - 1);
		}
		
		// Add a life
		plugin.getDataHandler().setLives(player, lives + 1);
		
		player.sendMessage(Lang.GAINED_A_LIFE.getConfigValue());
		
		event.setCancelled(true);
	}
}