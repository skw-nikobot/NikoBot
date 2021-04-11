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
					Core.pluginsMGR.pluginsClass.elementAt(i).first.onDisable();
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
					Core.pluginsMGR.pluginsClass.elementAt(i).first.onDisable();
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
			/*plugins*/
			}else if(cmd.getContentRaw().split(" ")[0].equals("/plugins")&&Core.ADMINS.isAdmin(event.getGuild().getId(), cmd)) {
				String[] cmdstr = cmd.getContentRaw().split(" ");
				if(cmdstr.length==1) {//Show plugins list
					String tmp = "";
					for(int i=0;i<Core.pluginsMGR.pluginsClass.size();i++) {
						String name = Core.pluginsMGR.pluginsClass.elementAt(i).first.pluginsName();
						
						if(Core.pluginsMGR.isAllowedGuild(event.getGuild().getId(), name))
							tmp+=":ballot_box_with_check:";
						else
							tmp+=":x:";
						tmp+=name;
						tmp+='\n';
					}
					Embed.EmbedSender(Color.pink, cmd.getChannel(), ":book: Plugins Active status", tmp);
				}else if(cmdstr.length>2&&cmdstr[1].equals("enable")) {//Enable plugin
					String name = "";
					for(int i=2;i<cmdstr.length;i++)
						name+=cmdstr[i];
					if(Core.pluginsMGR.isExist(name)&&Core.pluginsMGR.permissionCheck(event.getGuild().getId(), cmd.getAuthor().getId(), name)) {
						Core.pluginsMGR.editAllowedGuild(event.getGuild().getId(), name, "true");
						cmd.getChannel().sendMessage("Plugin "+name+" enable.").queue();
					}else {
						cmd.getChannel().sendMessage("Plugin not found or permission not enough.").queue();
					}
				}else if(cmdstr.length>2&&cmdstr[1].equals("disable")) {//Disable plugin
					String name = "";
					for(int i=2;i<cmdstr.length;i++)
						name+=cmdstr[i];
					if(Core.pluginsMGR.isExist(name)&&Core.pluginsMGR.permissionCheck(event.getGuild().getId(), cmd.getAuthor().getId(), name)) {
						Core.pluginsMGR.editAllowedGuild(event.getGuild().getId(), name, "false");
						cmd.getChannel().sendMessage("Plugin "+name+" disable.").queue();
					}else {
						cmd.getChannel().sendMessage("Plugin not found or permission not enough.").queue();
					}
				}

			}
			
		}	
	}
}
