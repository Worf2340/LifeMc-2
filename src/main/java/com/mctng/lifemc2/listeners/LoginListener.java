package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.regex.Pattern;

//Listener class
public class LoginListener implements Listener {

	private LifeMc2 plugin;

	public LoginListener(LifeMc2 instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		int lives;


		if (!plugin.getDataHandler().isStored(player.getName())) {
			// Check if player has a starting life permission
			Pattern p = Pattern.compile("lifemc.lives.starting.([^.]+$)");
			lives = plugin.getDataHandler().getLivesFromPermission(player, p);

			if (lives == 0){
				lives = plugin.getConfigHandler().getDefaultStartingLives();
			}

			plugin.getDataHandler().setLives(player, lives);


			//endregion

		} else {
			// Check already existing player
			lives = plugin.getDataHandler().getLives(player);

			if (lives < 1) {
				PlayerLoginEvent.Result result = PlayerLoginEvent.Result.KICK_BANNED;
				event.disallow(result, Lang.KICK_OUT_OF_LIVES.getConfigValue());
				
				event.setResult(result);
			}
		}

		plugin.getDataHandler().savePlayer(player);
	}

}