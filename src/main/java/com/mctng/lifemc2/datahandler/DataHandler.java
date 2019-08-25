package com.mctng.lifemc2.datahandler;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.config.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to get information about the lives of a player
 * <p>
 * Date created:  15:14:55
 * 7 mrt. 2014
 * @author Staartvin
 *
 */
public class DataHandler {

	private ConfigWrapper dataWrapper;
	private FileConfiguration dataConfig;
	
	private String accountHolder = "accounts";
	private LifeMc2 plugin;
	
	public DataHandler(LifeMc2 instance) {
		dataWrapper = instance.getDataConfig();
		dataConfig = instance.getDataConfig().getConfig();
		plugin = instance;
	}
	
	
	public int getLives(Player player) {
		UUID uuid = player.getUniqueId();
		
		return dataConfig.getInt(accountHolder + "." + uuid.toString() + ".lives", 0);
	}
	
	public int getLives(String playerName) {

		String uuid = getUUIDString(playerName);
		if (uuid == null) return 0;
		
		return dataConfig.getInt(accountHolder + "." + uuid + ".lives", 0);
	}

	public void setLives(Player player, int lives) {
		UUID uuid = player.getUniqueId();
		
		dataConfig.set(accountHolder + "." + uuid.toString() + ".lives", lives);
		dataConfig.set(accountHolder + "." + uuid.toString() + ".name", player.getName());

		// Save
		dataWrapper.saveConfig();	
	}

	
	public void setLives(String playerName, int lives) {
		String uuid = getUUIDString(playerName);
		if (uuid == null) return;

		dataConfig.set(accountHolder + "." + uuid + ".lives", lives);

		// Save
		dataWrapper.saveConfig();	
	}

	public void setLives(Player player, int lives, int maxLives, int startingLives) {
		UUID uuid = player.getUniqueId();

		dataConfig.set(accountHolder + "." + uuid.toString() + ".lives", lives);
		dataConfig.set(accountHolder + "." + uuid.toString() + ".name", player.getName());
		dataConfig.set(accountHolder + "." + uuid.toString() + ".max lives", maxLives);
		dataConfig.set(accountHolder + "." + uuid.toString() + ".starting lives", startingLives);

		// Save
		dataWrapper.saveConfig();
	}

	public int getMaxLives(String name) {
		if (plugin.getServer().getPlayerExact(name) != null){
			Player player = plugin.getServer().getPlayerExact(name);

			Pattern p = Pattern.compile("lifemc.lives.max.([^.]+$)");
			Matcher m;
			int lives = 0;

			lives = getLivesFromPermission(player, p);

			if (lives == 0) {
				return plugin.getConfigHandler().getDefaultMaxLives();
			}
			else {
				return lives;
			}
		}
		else {
			return dataConfig.getInt(accountHolder + "." + getUUIDString(name) + ".max lives", plugin.getConfigHandler().getDefaultMaxLives());
		}

	}

	public int getStartingLives(String name) {
		if (plugin.getServer().getPlayerExact(name) != null){
			Player player = plugin.getServer().getPlayerExact(name);

			Pattern p = Pattern.compile("lifemc.lives.starting.([^.]+$)");
			int lives = 0;

			lives = getLivesFromPermission(player, p);

			if (lives == 0) {
				return plugin.getConfigHandler().getDefaultStartingLives();
			}
			else {
				return lives;
			}
		}
		else {
			return dataConfig.getInt(accountHolder + "." + getUUIDString(name) + ".starting lives", plugin.getConfigHandler().getDefaultStartingLives());
		}

	}

	public int getLivesFromPermission(Player player, Pattern p){
		Matcher m;
		int lives = 0;
		for (PermissionAttachmentInfo permission : player.getEffectivePermissions()){
			m = p.matcher(permission.getPermission());
			if (m.matches()){
				if (m.group(1).matches("\\d+")) {
					if (Integer.parseInt(m.group(1)) > lives) {
						lives = Integer.parseInt(m.group(1));
					}
				}
			}
		}
		return lives;
	}

	public String getPlayerName(String uuid) {
		return dataConfig.getString(accountHolder + "." + uuid + ".name", null);
	}
	
	public String getUUIDString (String playerName) {
		if (dataConfig.getConfigurationSection(accountHolder) == null) {
			return null;
		}
		
		for (String uuid: dataConfig.getConfigurationSection(accountHolder).getKeys(false)) {
			String oldPlayerName = getPlayerName(uuid);
			
			if (uuid != null && oldPlayerName.equalsIgnoreCase(playerName)) {
				return uuid;
			}
		}
		
		return null;
	}

	public void resetPlayers () {
		for (String uuid : dataConfig.getConfigurationSection("accounts").getKeys(false)) {
			if (this.getLives(getPlayerName(uuid)) < 3) {
				this.setLives(getPlayerName(uuid), 3);
			}
		}
	}

	public void resetPlayersStarting () {
		for (String uuid : dataConfig.getConfigurationSection("accounts").getKeys(false)) {
			if (this.getLives(getPlayerName(uuid)) < getStartingLives(getPlayerName(uuid))) {
				this.setLives(getPlayerName(uuid), getStartingLives(getPlayerName(uuid)));
			}
		}
	}

	public int resetPlayerStarting (String playerName) {

		if (!(isStored(playerName)) && (Bukkit.getPlayerExact(playerName) == null)) {
			return -1;
		}

		if ((this.getLives(playerName)) < this.getStartingLives(playerName)) {
			this.setLives(playerName, this.getStartingLives(playerName));
			return 0;
		}
		return 1;
	}

	public void savePlayer(Player player){
		Pattern max = Pattern.compile("lifemc.lives.max.([^.]+$)");
		Pattern starting = Pattern.compile("lifemc.lives.starting.([^.]+$)");

		int maxLives = getLivesFromPermission(player, max);
		int startingLives = getLivesFromPermission(player, starting);

		if (maxLives == 0){
			maxLives = plugin.getConfigHandler().getDefaultMaxLives();
		}

		if (startingLives == 0){
			startingLives = plugin.getConfigHandler().getDefaultMaxLives();
		}

		plugin.getDataHandler().setLives(player, plugin.getDataHandler().getLives(player), maxLives, startingLives);
	}

	public boolean isStored(String playerName) {
		return getUUIDString(playerName) != null;
	}
}
