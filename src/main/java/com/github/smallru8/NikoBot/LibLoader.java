package com.github.smallru8.NikoBot;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Load libraries from ./libs/ and other custom libs.
 * ClassLoader structure : AppClassLoader << LIB << Ext_LIB << Plugin's URLClassLoader
 *
 */
public class LibLoader {
	
	/** JAR files in ./libs/ */
	public static URLClassLoader LIB;
	/** Other JAR files */
	public static URLClassLoader Ext_LIB;
	
	private String dir = "libs";
	private String[] libsPath;
	
	public LibLoader() throws IOException {
		fileCheck();
	}
	
	/**
	 * Check ./libs directory is exist.
	 * @throws IOException
	 */
	private void fileCheck() throws IOException {
		File libs = new File(dir);//libs
		if(!libs.exists())
			libs.mkdir();
		libsPath = libs.list();
		Arrays.sort(libsPath);
		if(!(new File("conf.d/extra-libs.yml").exists())) {//cfg/extra-libs.yml
			FileWriter fw = new FileWriter("conf.d/extra-libs.yml");
			fw.write("//Put your library path, using enter to split them.\n");
			fw.flush();
			fw.close();
		}
	}
	/**
	 * Load JAR files in ./libs/ to URLClassLoader LIB 
	 */
	public void load() {
		ArrayList<URL> urlTmp = new ArrayList<URL>();
		for(int i=0;i<libsPath.length;i++) {
			if(libsPath[i].endsWith(".jar")) {//only load jar file
				System.out.println("[LOAD][LIB]:Load "+libsPath[i]+".");
				try {
					urlTmp.add(new URL("file:libs/"+libsPath[i]));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					System.out.println("[WARN][LIB]:Failed. When loading " + libsPath[i]+".");
					e.printStackTrace();
				}
			}
		}
		URL[] urls = new URL[urlTmp.size()];
		for(int i=0;i<urlTmp.size();i++)
			urls[i] = urlTmp.get(i);
		urlTmp.clear();
		LIB = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Load other JAR files to URLClassLoader Ext_LIB 
	 */
	public void loadExtLibs() throws IOException{
		FileReader fr = new FileReader("conf.d/extra-libs.yml");
		BufferedReader br = new BufferedReader(fr);
		String tmp = null;
		ArrayList<URL> urlTmp = new ArrayList<URL>();
		while((tmp = br.readLine())!=null) {
			if((!tmp.startsWith("//"))&&tmp.endsWith(".jar")) {
				try {
					System.out.println("[LOAD][LIB]:Load "+tmp+".");
					urlTmp.add(new URL("file:"+tmp));
				} catch (Exception exp) {
					System.out.println("[WARN][LIB]:Failed. When loading " + tmp +".");
					exp.printStackTrace();
				}
			}
		}
		br.close();
		fr.close();
		URL[] urls = new URL[urlTmp.size()];
		for(int i=0;i<urlTmp.size();i++)
			urls[i] = urlTmp.get(i);
		urlTmp.clear();
		Ext_LIB = new URLClassLoader(urls,LIB);
	}
}
