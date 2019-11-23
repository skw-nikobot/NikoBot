package com.github.smallru8.NikoBot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class CfgReader {
	
	/**
	 * 
	 * @param path read cfg file from this path.
	 * @return data which split with '\n' in cgf file. 
	 */
	public static String[] readCfg(String path) {
		ArrayList<String> strLs = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String tmp = null;
			while((tmp = br.readLine())!=null) 
				strLs.add(tmp);
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] re = new String[strLs.size()];
		for(int i=0;i<strLs.size();i++)
			re[i] = strLs.get(i);
		strLs.clear();
		return re;
	}
	
	/**
	 * Read app's token from file.
	 * @return Discord app's token.
	 * @throws IOException
	 */
	public static String token() throws IOException {
		FileReader fr;
		String token = "";
		try {
			fr = new FileReader("cfg/token");
			BufferedReader br = new BufferedReader(fr);
			token = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[LOAD][CFG]:Load token.");
		return token;
	}
	
	/**
	 * Set bot's current status.
	 * @param jda
	 * @throws IOException
	 */
	public static void status(JDABuilder jda) throws IOException {
		
		FileReader fr;
		String[] status = null;
		try {
			fr = new FileReader("cfg/status");
			BufferedReader br = new BufferedReader(fr);
			br.readLine();
			status = br.readLine().split("=");
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(status[1].equals("DO_NOT_DISTURB"))
			jda.setStatus(OnlineStatus.DO_NOT_DISTURB);
		else if(status[1].equals("IDLE"))
			jda.setStatus(OnlineStatus.IDLE);
		else if(status[1].equals("INVISIBLE"))
			jda.setStatus(OnlineStatus.INVISIBLE);
		else if(status[1].equals("OFFLINE"))
			jda.setStatus(OnlineStatus.OFFLINE);
		else if(status[1].equals("ONLINE"))
			jda.setStatus(OnlineStatus.ONLINE);
		else
			jda.setStatus(OnlineStatus.UNKNOWN);
		System.out.println("[LOAD][CFG]:Set status.");
	}
	
	/**
	 * Read game name from file.
	 * @return Game name which read from file.
	 * @throws IOException
	 */
	public static String game() throws IOException {
		
		FileReader fr;
		String game = "";
		try {
			fr = new FileReader("cfg/game");
			BufferedReader br = new BufferedReader(fr);
			game = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[LOAD][CFG]:Set game.");
		return game;
		
	}
	
	/**
	 * Read bot's name from file.
	 * @return bot's name.
	 * @throws IOException
	 */
	public static String botName() throws IOException {
		
		FileReader fr;
		String name = "";
		try {
			fr = new FileReader("cfg/name");
			BufferedReader br = new BufferedReader(fr);
			name = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[LOAD][CFG]:Load botname.");
		return name;
		
	}
}
