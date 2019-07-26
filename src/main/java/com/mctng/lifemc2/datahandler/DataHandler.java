package com.mctng.lifemc2.datahandler;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.config.ConfigWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

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

	public void setLives(Player player, int lives, int maxLives) {
		UUID uuid = player.getUniqueId();

		dataConfig.set(accountHolder + "." + uuid.toString() + ".lives", lives);
		dataConfig.set(accountHolder + "." + uuid.toString() + ".name", player.getName());
		dataConfig.set(accountHolder + "." + uuid.toString() + ".max lives", plugin.getConfigHandler().getMaxLives(player.getName()));

		// Save
		dataWrapper.saveConfig();
	}

	public int getMaxLives(String playerName) {
		String uuid = getUUIDString(playerName);

		if (uuid == null) return 0;

		return dataConfig.getInt(accountHolder + "." + uuid + ".max lives", 10);
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
	
	public boolean isStored(String playerName) {
		return getUUIDString(playerName) != null;
	}
}
