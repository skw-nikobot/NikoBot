package com.github.smallru8.NikoBot.commands;

import java.awt.Color;
import java.io.IOException;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.Embed;
import com.github.smallru8.NikoBot.Setting.Setting;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SysCMD extends ListenerAdapter{
	
	/**
	 * System commands.
	 */
	public void onMessageReceived(MessageReceivedEvent event){
		Message cmd = event.getMessage();
		if(!(cmd.getAuthor().isBot())&&cmd.getContentRaw().startsWith("/")) {
			
			/** Shutdown bot.*/
			if(cmd.getContentRaw().split(" ")[0].equals("/shutdown")&&Core.ADMINS.isAdmin("", cmd)) {
				for(int i=0;i<Core.pluginsMGR.pluginsClass.size();i++) {
					Core.pluginsMGR.pluginsClass.elementAt(i).onDisable();
				}
				Embed.EmbedSender(Color.pink,cmd.getChannel(),":octagonal_sign: おやすみなさい! System shutdown!","");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			
			/** Reboot bot.*/
			}else if(cmd.getContentRaw().split(" ")[0].equals("/reboot")&&Core.ADMINS.isAdmin("", cmd)) {
				for(int i=0;i<Core.pluginsMGR.pluginsClass.size();i++) {
					Core.pluginsMGR.pluginsClass.elementAt(i).onDisable();
				}
				Embed.EmbedSender(Color.pink,cmd.getChannel(),":octagonal_sign: リロード! System reboot!","");
				try {
					if(!Core.osType)
						Runtime.getRuntime().exec("sh ./restart.sh");//linux
					else
						Runtime.getRuntime().exec("./restart.bat");//windows
					
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
				
			/** Change bot's status to idle.*/
			}else if(cmd.getContentRaw().split(" ")[0].equals("/sleep")&&!Core.sleepFlag&&Core.ADMINS.isAdmin("", cmd)) {
				/*Sleep*/
				Core.botAPI.getPresence().setStatus(OnlineStatus.IDLE);
				Core.sleepFlag = true;
				Embed.EmbedSender(Color.pink,cmd.getChannel(),":red_circle: Sleep mode.","");
				
			/** Change bot's status to online.*/
			}else if(cmd.getContentRaw().split(" ")[0].equals("/wake")&&Core.sleepFlag&&Core.ADMINS.isAdmin("", cmd)) {
				/*Wake*/
				Core.botAPI.getPresence().setStatus(OnlineStatus.ONLINE);
				Core.sleepFlag = false;
				Embed.EmbedSender(Color.pink,cmd.getChannel(),":large_blue_circle: Normal mode.","");
				
			/** Set bot's current playing game's name.*/
			}else if(cmd.getContentRaw().split(" ")[0].equals("/setgame")&&Core.ADMINS.isAdmin("", cmd)) {
				/*Set game*/
				String[] cmdstr = cmd.getContentRaw().split(" ");
				if(cmdstr.length >=2) {
					Core.configC.saveStatus("game", cmdstr[1]);
					Core.botAPI.getPresence().setActivity(Activity.playing(Setting.GAME));
				}else
					cmd.getChannel().sendMessage("Usage:/setgame <Game>");
			}	
		}	
	}
}
