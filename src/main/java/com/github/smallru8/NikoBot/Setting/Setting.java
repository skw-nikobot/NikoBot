package com.github.smallru8.NikoBot.Setting;

import com.github.smallru8.NikoBot.StdOutput;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class Setting {
	
	public static String TOKEN = "";
	public static String STATUS = "";
	public static String GAME = "";
	
	public static void status(JDABuilder jda){
		
		if(STATUS.equals("donotdisturb"))
			jda.setStatus(OnlineStatus.DO_NOT_DISTURB);
		else if(STATUS.equals("idle"))
			jda.setStatus(OnlineStatus.IDLE);
		else if(STATUS.equals("invisible"))
			jda.setStatus(OnlineStatus.INVISIBLE);
		else if(STATUS.equals("offline"))
			jda.setStatus(OnlineStatus.OFFLINE);
		else if(STATUS.equals("online"))
			jda.setStatus(OnlineStatus.ONLINE);
		else
			jda.setStatus(OnlineStatus.UNKNOWN);
		StdOutput.infoPrintln("Set status.");
	}
}
