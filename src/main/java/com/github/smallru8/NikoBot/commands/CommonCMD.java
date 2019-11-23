package com.github.smallru8.NikoBot.commands;

import java.awt.Color;

import com.github.smallru8.NikoBot.CfgReader;
import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.Embed;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommonCMD extends ListenerAdapter{
	
	/**
	 * Commands /info and /help 
	 */
	public void onMessageReceived(MessageReceivedEvent event){
		Message cmd = event.getMessage();
		if(!(cmd.getAuthor().isBot())&&cmd.getContentRaw().startsWith("/")) {
			String[] cmds = cmd.getContentRaw().split(" ");
			if(cmds[0].equalsIgnoreCase("/info")) {
				/*info*/
				String[] info = CfgReader.readCfg("cfg/info");
				String sum = "";
				for(String ele:info) 
					sum+=(ele+"\n");
				Embed.EmbedSender(Color.pink, cmd.getChannel(), ":information_source: Info:", sum);
			}else if(cmds[0].equalsIgnoreCase("/help")) {
				/*help*/
				String[] help = CfgReader.readCfg("cfg/help");
				String sum = "";
				for(String ele:help)
					sum+=(ele+"\n");
				for(String ele:Core.pluginsMGR.helpLs)//plugin's help
					sum+=ele;
				Embed.EmbedSender(Color.pink, cmd.getChannel(), ":regional_indicator_h::regional_indicator_e::regional_indicator_l::regional_indicator_p:", sum);
			}
		}
	}
}
