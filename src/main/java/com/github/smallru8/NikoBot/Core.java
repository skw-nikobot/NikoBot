package com.github.smallru8.NikoBot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.security.auth.login.LoginException;

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
 * @version 1911.2
 */
public class Core {
	
	public static String botName = "";
	public static boolean sleepFlag = false;
	public static boolean osType = false;//windows = true;Linux = false;
	public static LibLoader libLoad;
	public static JDA botAPI;
	public static PermissionManager PM;
	public static PluginsManager pluginsMGR;
	public static void main( String[] args ) throws IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
		CfgChecker.cfgCheck();
		libLoad = new LibLoader();
		libLoad.load();
		libLoad.loadExtLibs();
		PM = new PermissionManager();
		
		//JDA jda = JDABuilder.createDefault(CfgReader.token()).build();
		//JDABuilder jda = new JDABuilder(AccountType.BOT);
		JDABuilder jda = JDABuilder.createDefault("");
		jda.setToken(CfgReader.token());
		jda.setAutoReconnect(true);
		jda.addEventListeners(new EventSender());
		jda.addEventListeners(new SysCMD());
		jda.addEventListeners(new CommonCMD());
		CfgReader.status(jda);
		botName = CfgReader.botName();
		try {
			botAPI = jda.build();
			botAPI.getPresence().setActivity(Activity.playing(CfgReader.game()));
		} catch (LoginException e) {
			e.printStackTrace();
		}
		pluginsMGR = new PluginsManager();
		pluginsMGR.loadPlugins();
		pluginsMGR.setUpPlugins();
    }
}
