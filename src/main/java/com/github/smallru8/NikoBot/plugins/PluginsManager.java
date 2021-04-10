package com.github.smallru8.NikoBot.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.Vector;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.LibLoader;
import com.github.smallru8.util.Pair;

public class PluginsManager {
	
	/** Plugins' path.*/
	private Vector<String> jarUrl;
	/** All loaded plugins.*/
	public Vector<Pair<PluginsInterface,String>> pluginsClass;
	
	public PluginsManager(){
		jarUrl = new Vector<String>();
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
				jarUrl.addElement("file:plugins/" + fileLs[i]);
				System.out.println("[INFO][PLUGINS]:Load " + fileLs[i] + ".");
			}
		}
		System.out.println("[INFO][PLUGINS]:" + jarUrl.size() + " plugins have been loaded.");
	}
	
	/**
	 * Load JAR files to URLClassLoader and set their parents to Ext_LIB.
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void setUpPlugins() throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		pluginsClass.clear();
		System.out.print("[INFO][PLUGINS]:Plugins setup...\n");
		for(int i=0;i<jarUrl.size();i++) {
			try {
				
				URL url = new URL(jarUrl.elementAt(i));
				@SuppressWarnings("resource")
				URLClassLoader tmpClassLoader = new URLClassLoader(new URL[] { url }, LibLoader.Ext_LIB); 
				
				InputStream is = tmpClassLoader.findResource("Niko.yml").openStream();
				/**
				 * path=com.github.smallru8.......
				 * startPermission=local/global
				 */
				Properties pro = new Properties();
				pro.load(is);
				String loadPath = pro.getProperty("path");
				String startPermission = pro.getProperty("startPermission","global");
				is.close();
				
				Class<?> tmpClass = tmpClassLoader.loadClass(loadPath);///第一個切入點

				PluginsInterface tmpInterface = (PluginsInterface) tmpClass.getDeclaredConstructor().newInstance();///創建plugins class
				Pair<PluginsInterface,String> pair = new Pair<PluginsInterface,String>();
				pair.makePair(tmpInterface, startPermission);
				pluginsClass.addElement(pair);
				tmpInterface.onEnable();
				
			} catch (MalformedURLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.print("[INFO][PLUGINS]:Plugins setup done! Total plugins:" + jarUrl.size() + ".\n");
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
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			
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
