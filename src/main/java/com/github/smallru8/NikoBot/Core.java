package com.github.smallru8.NikoBot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.security.auth.login.LoginException;

import com.github.smallru8.NikoBot.SQL.AdminData;
import com.github.smallru8.NikoBot.Setting.CfgChecker;
import com.github.smallru8.NikoBot.Setting.Setting;
import com.github.smallru8.NikoBot.commands.CommonCMD;
import com.github.smallru8.NikoBot.commands.SysCMD;
import com.github.smallru8.NikoBot.event.EventSender;
import com.github.smallru8.NikoBot.plugins.PluginsManager;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

/**
 * NikoBot
 * @author smallru8
 * @version 2104.1
 */
public class Core {
	
	public static CfgChecker configC;
	public static JDA botAPI;
	
	public static boolean sleepFlag = false;
	public static boolean osType = false;//windows = true;Linux = false;
	
	public static LibLoader libLoad;
	
	public static AdminData ADMINS;
	public static PluginsManager pluginsMGR;
	public static void main( String[] args ) throws IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
		/*基本設定*/
		configC = new CfgChecker();
		configC.loadAll();
		
		libLoad = new LibLoader();
		libLoad.load();
		libLoad.loadExtLibs();
		
		ADMINS = new AdminData();
		
		JDABuilder jda = JDABuilder.createDefault(Setting.TOKEN);
		jda.setAutoReconnect(true);
		jda.addEventListeners(new EventSender());
		jda.addEventListeners(new SysCMD());
		jda.addEventListeners(new CommonCMD());
		
		Setting.status(jda);
		
		try {
			botAPI = jda.build();
			botAPI.getPresence().setActivity(Activity.playing(Setting.GAME));
		} catch (LoginException e) {
			e.printStackTrace();
		}
		
		pluginsMGR = new PluginsManager();
		pluginsMGR.loadPlugins();
		pluginsMGR.setUpPlugins();
    }
}
