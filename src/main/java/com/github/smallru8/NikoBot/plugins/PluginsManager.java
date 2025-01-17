package com.github.smallru8.NikoBot.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.skunion.smallru8.util.Pair;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.StdOutput;

public class PluginsManager {
	
	/** Plugins' path.*/
	private ArrayList<URL> jarUrl;
	/** All loaded plugins.*/
	public Vector<Pair<PluginsInterface,String>> pluginsClass;
	
	private URLClassLoader pluginsClassLoader;
	
	public PluginsManager(){
		jarUrl = new ArrayList<URL>();
		pluginsClass = new Vector<Pair<PluginsInterface,String>>();
	}
	
	/**
	 * Load all plugins' path to jarUrl.
	 */
	public void loadPlugins() {
		jarUrl.clear();
		File pluginsList = new File("plugins/");
		String[] fileLs = pluginsList.list();
		for(int i = 0;i<fileLs.length;i++) {///取得jar數量(過濾其他檔案)
			if(fileLs[i].indexOf(".jar") != -1) {
				try {
					jarUrl.add(new URI("file:plugins/" + fileLs[i]).toURL());
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("[INFO][PLUGINS]:Load " + fileLs[i] + ".");
			}
		}
		System.out.println("[INFO][PLUGINS]:" + jarUrl.size() + " plugins have been loaded.");
		pluginsClassLoader =  new URLClassLoader(jarUrl.toArray(new URL[0]), Core.class.getClassLoader()); 
	}
	
	/**
	 * Load JAR files to URLClassLoader and set their parents to Ext_LIB.
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void setUpPlugins(){
		pluginsClass.clear();
		StdOutput.infoPrintln("Plugins setup...");
		
		try {
		
			Enumeration<URL> niko_yml = pluginsClassLoader.findResources("Niko.yml");
			
			while(niko_yml.hasMoreElements()) {
				InputStream is = niko_yml.nextElement().openStream();
				/**
				 * path=com.github.smallru8.......
				 * startPermission=local/global
				 */
				Properties pro = new Properties();
				pro.load(is);
				String loadPath = pro.getProperty("path");
				String startPermission = pro.getProperty("startPermission","global");
				is.close();
				
				Class<?> tmpClass = pluginsClassLoader.loadClass(loadPath);///第一個切入點
	
				PluginsInterface tmpInterface = (PluginsInterface) tmpClass.getDeclaredConstructor().newInstance();///創建plugins class
				Pair<PluginsInterface,String> pair = new Pair<PluginsInterface,String>();
				pair.makePair(tmpInterface, startPermission);
				pluginsClass.addElement(pair);
				tmpInterface.onEnable();
				is.close();
			}
		}catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		StdOutput.infoPrintln("Plugins setup done! Total plugins:" + jarUrl.size());
	}
	
	/**
	 * verify if the guild has allowed select plugin
	 * For plugins which not allow every guild to use call
	 * servers/<serverID>/allowedPlugins.conf
	 * @param serverID
	 * @param pluginName
	 * @return
	 */
	public boolean isAllowedGuild(String serverID,String pluginName) {
		File f = new File("servers/" + serverID + "/allowedPlugins.conf");
		if(!f.exists()) {
			try {
				FileWriter fw = new FileWriter(f);
				fw.write("");
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Properties pro = new Properties();
			InputStream in = new FileInputStream("servers/" + serverID + "/allowedPlugins.conf");
			pro.load(in);
			in.close();
			String pluginAllow = pro.getProperty(pluginName, "false");
			if(pluginAllow.equalsIgnoreCase("true"))
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void editAllowedGuild(String serverID,String pluginName,String value) {
		Properties pro = new Properties();
		try {
			InputStream in = new FileInputStream("servers/" + serverID + "/allowedPlugins.conf");
			pro.load(in);
			in.close();
			pro.setProperty(pluginName, value);
			OutputStream out = new FileOutputStream("servers/" + serverID + "/allowedPlugins.conf");
			pro.store(out, "");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if user has enough permission to do this.
	 * @param serverID
	 * @param userID
	 * @param pluginName
	 * @return
	 */
	public boolean permissionCheck(String serverID,String userID,String pluginName) {
		String per = "";
		for(int i=0;i<pluginsClass.size();i++) {
			if(pluginsClass.elementAt(i).first.pluginsName().equals(pluginName)) {
				per = pluginsClass.elementAt(i).second;
				break;
			}
		}
		
		if(per.equalsIgnoreCase("global")) {//Need global admin to edit
			return Core.ADMINS.isAdmin("", userID);
		}else if(per.equalsIgnoreCase("local")) {//Need local admin to edit
			return Core.ADMINS.isAdmin(serverID, userID);
		}
		
		return false;
	}
	
	public boolean isExist(String pluginName) {
		for(int i=0;i<pluginsClass.size();i++) 
			if(pluginsClass.elementAt(i).first.pluginsName().equals(pluginName))
				return true;	
		return false;
	}
}
