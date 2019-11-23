package com.github.smallru8.NikoBot;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

public class CfgChecker {
	public enum WriteMode{
		COVER,APPEND
	}
	/**
	 * Sava cfg changes
	 */
	public static boolean updateCfg(String path,String[] lines,WriteMode wm) {
		try {
			FileWriter fw = new FileWriter(path);
			if(wm==WriteMode.COVER) {
				for(String str:lines)
					fw.write(str + "\n");
				fw.flush();
			}else if(wm==WriteMode.APPEND) {
				for(String str:lines)
					fw.append(str + "\n");
				fw.flush();
			}
			fw.close();	
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	/**
	 * Check cfg is exist
	 */
	public static boolean cfgCheck() throws IOException {
		
		String osName = System.getProperties().getProperty("os.name");
		System.out.println("[INFO][OS]:"+osName);
		if(osName.indexOf("Windows") != -1||osName.indexOf("windows") != -1) {
			Core.osType = true;
		}
		
		boolean flag = true;
		if(!Core.osType) {
			File restart = new File("restart.sh");
			if(!restart.exists()) {
				FileWriter restart_ = new FileWriter("restart.sh");
				restart_.write("sleep 10" + "\r\n");
				restart_.write("nohup java -Dfile.encoding=UTF8 -jar NikoBot.jar");
				restart_.flush();
				restart_.close();
			}
		}else {
			File restart = new File("restart.bat");
			if(!restart.exists()) {
				FileWriter restart_ = new FileWriter("restart.bat");
				restart_.write("sleep 10" + "\r\n");
				restart_.write("java -Dfile.encoding=UTF8 -jar NikoBot.jar");
				restart_.flush();
				restart_.close();
			}
		}
		File cfg = new File("cfg");
		if(!cfg.exists()) {
	        cfg.mkdirs();
	        FileWriter admin = new FileWriter("cfg/admin");
	        admin.write("000066123420740364\r\n" + "000059081259843584");
	        admin.flush();
	        admin.close();
	        System.out.println("[INFO][CFG]:Create admin list.");
	        
	        FileWriter name_ = new FileWriter("cfg/name");
			name_.write("YourAppName");
			name_.flush();
			name_.close();
			System.out.println("[INFO][CFG]:Create botname file.");
			FileWriter game_ = new FileWriter("cfg/game");
			game_.write("NIKOBOT");
			game_.flush();
			game_.close();
			System.out.println("[INFO][CFG]:Create game file.");
			FileWriter help_ = new FileWriter("cfg/help");
			help_.write("/help");
			help_.flush();
			help_.close();
			System.out.println("[INFO][CFG]:Create help file.");
			FileWriter info = new FileWriter("cfg/info");
			info.write("/info");
			info.flush();
			info.close();
			System.out.println("[INFO][CFG]:Create info file.");
			
			FileWriter status_ = new FileWriter("cfg/status");
			status_.write("[DO_NOT_DISTURB,IDLE,INVISIBLE,OFFLINE,ONLINE,UNKNOWN]\r\n" + "status=ONLINE");
			status_.flush();
			status_.close();
			System.out.println("[INFO][CFG]:Create status file.");
			FileWriter token_ = new FileWriter("cfg/token");
			token_.write("NIKOBOTc3NTA0MjcyNDAwMzg0.ABCD-w.EXAMPLE8djM25kSMALLRU8qC28U");
			token_.flush();
			token_.close();
			System.out.println("[INFO][CFG]:Create token file.");
			flag = false;
		}
		
		File plugins = new File("plugins");
		if(!plugins.exists()) {
			plugins.mkdirs();
			System.out.println("[INFO][CFG]:Create plugins dir.");
		}
		System.out.println("[INFO][CFG]:CFG check done.");
		return flag;
	}
}
