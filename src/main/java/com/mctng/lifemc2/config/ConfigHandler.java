package com.mctng.lifemc2.config;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.fileutil.FileUtil;
import com.mctng.lifemc2.lang.Lang;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date created: 22:05:12
 * 24 jan. 2014
 * 
 * @author Staartvin
 * 
 */
public class ConfigHandler {

	private LifeMc2 plugin;
	private FileConfiguration mainConfig;

	public ConfigHandler(LifeMc2 instance) {
		plugin = instance;
	}
	
	public void loadMainConfig() {
		
		mainConfig = FileUtil.loadFile("config.yml", "config.yml");

		plugin.getLogger().info("Loaded config.yml");
	}
	
	public void loadLangConfig() {
		FileConfiguration langConfig = plugin.getLangConfig().getConfig();
		
		// Add reference for lang class
		Lang.setFile(langConfig);
		
		for (Lang message: Lang.values()) {
			langConfig.addDefault(message.getPath(), message.getDefault());
		}
		
		langConfig.options().copyDefaults(true);
		
		plugin.getLangConfig().saveConfig();
	}
 	
	public boolean isBuyingEnabled() {
		return mainConfig.getBoolean("Buying enabled", false);
	}
	
	public boolean isRevivingEnabled() {
		return mainConfig.getBoolean("Revive enabled", false);
	}
	
	public boolean isEatingEnabled() {
		return mainConfig.getBoolean("Eating enabled", false);
	}
	
	public List<Material> getEatableItems() {
		List<String> fakeMaterials = mainConfig.getStringList("Eatable items");
		List<Material> realMaterials = new ArrayList<Material>();
		
		for (String fakeMaterial: fakeMaterials) {
			Material mat = Material.getMaterial(fakeMaterial.toUpperCase().trim());
			
			if (mat != null) {
				realMaterials.add(mat);
			}
		}
		
		return realMaterials;
	}
	
	public int getMaxLives(String name) {
		//UUID uuid = UUID.fromString(uuidString);
		if (plugin.getServer().getPlayerExact(name) != null){
			Player player = plugin.getServer().getPlayerExact(name);

			Pattern p = Pattern.compile("lifemc.lives.max.([^.]+$)");
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

			if (lives == 0) {
				return mainConfig.getInt("Max lives", 10);
			}
			else {
				return lives;
			}
		}
		else {
			return plugin.getDataHandler().getMaxLives(name);
		}

	}

	public int getMaxLives() {
		return mainConfig.getInt("Max lives", 10);
	}
	
	public int getLifeCost() {
		return mainConfig.getInt("Life cost", 100);
	}
	
	public int getStartingLives() {
		return mainConfig.getInt("Starting lives", 3);
	}
	
	public boolean spawnAtBedAfterBan() {
		return mainConfig.getBoolean("Spawn at bad after ban", false);
	}
	
	public boolean gainLifeAtMurder() {
		return mainConfig.getBoolean("Gain life at murder", false);
	}
}
