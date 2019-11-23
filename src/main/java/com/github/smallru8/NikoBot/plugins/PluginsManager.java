package com.github.smallru8.NikoBot.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import com.github.smallru8.NikoBot.LibLoader;

public class PluginsManager {
	
	/** Plugins' path.*/
	private Vector<String> jarUrl;
	/** All loaded plugins.*/
	public Vector<PluginsInterface> pluginsClass;
	/** All plugins' "Help".(Niko.yml)*/
	public Vector<String> helpLs;
	
	public PluginsManager(){
		jarUrl = new Vector<String>();
		pluginsClass = new Vector<PluginsInterface>();
		helpLs = new Vector<String>();
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
				
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String firstLoad = "";//Plugin's method path.
				firstLoad = br.readLine();
				
				String help = "",tmp = null;
				while((tmp = br.readLine())!=null)
					help+=(tmp+"\n");
				helpLs.add(help);//Help說明
				is.close();
				br.close();

				Class<?> tmpClass = tmpClassLoader.loadClass(firstLoad);///第一個切入點

				PluginsInterface tmpInterface = (PluginsInterface) tmpClass.getDeclaredConstructor().newInstance();///創建plugins class
				pluginsClass.addElement(tmpInterface);
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
}
