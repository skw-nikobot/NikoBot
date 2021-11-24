package com.github.smallru8.NikoBot.Setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.StdOutput;
import com.github.smallru8.NikoBot.commands.Help;
import com.github.smallru8.NikoBot.commands.Info;

import net.dv8tion.jda.api.entities.Guild;

public class CfgChecker {
	
	public CfgChecker() {
		StdOutput.infoPrintln("Checking config file...");
		File f = new File("conf.d");
		FileWriter fw;
		try {
			if(!f.exists())
				f.mkdir();
			f = new File("servers");
			if(!f.exists())
				f.mkdir();
			f = new File("conf.d/status");
			if(!f.exists()) {
				StdOutput.infoPrintln("Create: " + f.getName());
				fw = new FileWriter(f);
				fw.write("#Game status\n");
				fw.write("game=Play game\n");
				fw.write("#discord status (online,unknown,offline,invisible,idle,donotdisturb)\n");
				fw.write("status=online\n");
				fw.flush();
				fw.close();
			}
			f = new File("conf.d/admin");
			if(!f.exists()) {
				StdOutput.infoPrintln("Create: " + f.getName());
				fw = new FileWriter(f);
				fw.write("222650000259843584\n");
				fw.flush();
				fw.close();
			}
			f = new File("conf.d/token");
			if(!f.exists()) {
				StdOutput.infoPrintln("Create: " + f.getName());
				fw = new FileWriter(f);
				fw.write("#Put your token below this line.\n");
				fw.flush();
				fw.close();
				StdOutput.warnPrintln("token file is empty.");
			}
			f = new File("plugins");
			if(!f.exists()) {
				f.mkdirs();
			}
			f = new File("conf.d/servers");
			if(!f.exists()) {
				f.mkdirs();
			}
			f = new File("SQL");
			if(!f.exists()) {
				f.mkdirs();
			}
			f = new File("SQL/sql.conf");
			if(!f.exists()) {
				StdOutput.infoPrintln("Create: " + f.getName());
				fw = new FileWriter(f);
				fw.write("mySQL=false\n");
				fw.write("host=127.0.0.1\n");
				fw.write("port=3306\n");
				fw.write("db=NikoBot\n");
				fw.write("username=root\n");
				fw.write("passwd=rootpasswd\n");
				fw.flush();
				fw.close();
			}
			generateRestartScript();
			StdOutput.infoPrintln("Checking config file...DONE!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAll() {
		loadToken();
		loadStatus();
	}
	
	public void loadToken() {
		try {
			FileReader fr = new FileReader("conf.d/token");
			BufferedReader br = new BufferedReader(fr);
			String token = "";
			
			while((token=br.readLine())!=null&&token.startsWith("#"));
			//System.out.println(token);
			
			//while((token=br.readLine()).startsWith("#"));
			
			Setting.TOKEN = token;
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadStatus() {
		try {
			Properties pro = new Properties();
			InputStream is = new FileInputStream("conf.d/status");
			pro.load(is);
			Setting.GAME = pro.getProperty("game","Play game");
			Setting.STATUS = pro.getProperty("status","online");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveStatus(String key,String value) {
		try {
			Properties pro = new Properties();
			InputStream is = new FileInputStream("conf.d/status");
			OutputStream os = new FileOutputStream("conf.d/status");
			pro.load(is);
			pro.setProperty(key, value);
			pro.store(os, "");
			loadStatus();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void buildServerConf(ArrayList<Guild> list) {
		for(int i=0;i<list.size();i++) {
			StdOutput.infoPrintln("Set DC server: "+list.get(i).getId());
			if(!(new File("servers/"+list.get(i).getId()).exists())) {
				String guildID = list.get(i).getId();
				StdOutput.infoPrintln("Join server : " + guildID);
				Core.ADMINS.addTable(guildID);
				Core.ADMINS.addAdmin(guildID, list.get(i).getOwnerId());
				File f = new File("servers/"+guildID);
				f.mkdir();
				f = new File("servers/" + guildID + "/allowedPlugins.conf");
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
				Info info = new Info(guildID);
				if(!info.hasBeenSet())
					info.setInfo("Using /info set <text> to set info.");
				Help help = new Help(guildID);
				if(!help.hasBeenSet())
					help.setHelp("Using /help set <text> to set help.");
			}
		}
	}
	
	private void generateRestartScript() throws IOException {
		String startMain = "java -Dfile.encoding=UTF8 -jar "+System.getProperty("java.class.path");	
		if(Core.osType){//WIN
			FileWriter fw = new FileWriter(new File("restart.bat"));
			fw.write("timeout /t 10\n "+startMain);
			fw.flush();
			fw.close();
		}else {
			FileWriter fw = new FileWriter(new File("restart.sh"));
			fw.write("sleep 10\nnohup "+startMain);
			fw.flush();
			fw.close();
		}
	}
	
}
