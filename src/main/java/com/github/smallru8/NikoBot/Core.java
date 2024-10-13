package com.github.smallru8.NikoBot;

import java.util.ArrayList;

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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * NikoBot3
 * @author smallru8
 * @version 3.6.2112.1
 */
public class Core {
	
	public static String version = "4.0.2410.1";//2024.10.13
	
	public static CfgChecker configC;
	public static JDA botAPI;
	
	public static boolean sleepFlag = false;
	public static boolean osType = false;//windows = true;Linux = false;
	
	public static AdminData ADMINS;
	public static PluginsManager pluginsMGR;
	
	public static void main( String[] args ) throws InterruptedException
    {
		new Core().loaderEntryPoint();
    }
	
	/**
	 * 使用NikoBootLoader啟動
	 */
	public void loaderEntryPoint() {
		String osName = System.getProperties().getProperty("os.name");
		if(osName.indexOf("Windows") != -1||osName.indexOf("windows") != -1) {//windows
			osType = true;
		}else {
			osType = false;
		}
		/*基本設定*/
		configC = new CfgChecker();
		configC.loadAll();
		
		ADMINS = new AdminData();
		
		JDABuilder jda = JDABuilder.createDefault(Setting.TOKEN);
		jda.setAutoReconnect(true);
		jda.enableIntents(GatewayIntent.MESSAGE_CONTENT);
		jda.addEventListeners(new EventSender());
		jda.addEventListeners(new SysCMD());
		jda.addEventListeners(new CommonCMD());
		
		Setting.status(jda);
		
		botAPI = jda.build();
		botAPI.getPresence().setActivity(Activity.playing(Setting.GAME));
		
		try {
			botAPI.awaitReady();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		configC.buildServerConf(new ArrayList<Guild>(botAPI.getGuilds()));
		
		pluginsMGR = new PluginsManager();
		pluginsMGR.loadPlugins();
		pluginsMGR.setUpPlugins();
	}
	
}
