package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.regex.Matcher;
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
		System.out.println(player.getEffectivePermissions());
		int lives;

		System.out.println(plugin.getConfigHandler().getMaxLives(player.getName()));


		if (!plugin.getDataHandler().isStored(player.getName())) {
			// Check if player has a starting life permission
			Pattern p = Pattern.compile("lifemc.lives.starting.([^.]+$)");
			Matcher m;
			lives = 0;

			for (PermissionAttachmentInfo permission : player.getEffectivePermissions()){
				m = p.matcher(permission.getPermission());
				if (m.matches()){
					if (m.group(1).matches("\\d+")) {
						System.out.println(m.group(1));
						if (Integer.parseInt(m.group(1)) > lives) {
							lives = Integer.parseInt(m.group(1));
						}
					}
				}
			}

			if (lives == 0){
				lives = plugin.getConfigHandler().getStartingLives();
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
	}

}