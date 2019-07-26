package com.mctng.lifemc2.api;

import com.mctng.lifemc2.LifeMc2;
import com.mctng.lifemc2.config.ConfigHandler;
import com.mctng.lifemc2.datahandler.DataHandler;

/**
 * API class of LifeMc2
 * <p>
 * Date created: 18:25:09 9 sep. 2014
 * 
 * @author Staartvin
 *
 */
public class API {

	private LifeMc2 plugin;

	public API(LifeMc2 plugin) {
		this.plugin = plugin;
	}

	public DataHandler getDataHandler() {
		return plugin.getDataHandler();
	}
	
	public ConfigHandler getConfigHandler() {
		return plugin.getConfigHandler();
	}
	
}
