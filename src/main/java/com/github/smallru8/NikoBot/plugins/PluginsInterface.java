package com.github.smallru8.NikoBot.plugins;

public interface PluginsInterface {
	
	/**
	 * While plugin is loaded, this method will be done. 
	 */
	public void onEnable();
	
	/**
	 * While bot shutdown or reboot, this method will be done.
	 */
	public void onDisable();
	
	/**
	 * 
	 * @return plugin's name.
	 */
	public String pluginsName();
	
}
