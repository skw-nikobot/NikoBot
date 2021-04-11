package com.github.smallru8.NikoBot.commands;

import java.awt.Color;

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
			
			/*info*/
			if(cmds[0].equalsIgnoreCase("/info")) {
				if(Core.ADMINS.isAdmin(event.getGuild().getId(), cmd)) {
					if(cmds.length > 2&&cmds[1].equals("set")) {
						Info info = new Info(event.getGuild().getId());
						String sum = "";
						for(int i=2;i<cmds.length;i++) {
							sum+=cmds[i];
						}
						info.setInfo(sum);
						cmd.getChannel().sendMessage("New Info has saved. Using /info to show.").queue();
					}else if(cmds.length==1){
						Info info = new Info(event.getGuild().getId());
						Embed.EmbedSender(Color.pink, cmd.getChannel(), ":regional_indicator_i::regional_indicator_n::regional_indicator_f::regional_indicator_o:", info.getInfo());
					}else {
						cmd.getChannel().sendMessage("Command not found.").queue();
					}
				}
				
			/*help*/
			}else if(cmds[0].equalsIgnoreCase("/help")) {
				if(cmds.length > 2&&cmds[1].equals("set")&&Core.ADMINS.isAdmin(event.getGuild().getId(), cmd)) {
					Help help = new Help(event.getGuild().getId());
					String sum = "";
					for(int i=2;i<cmds.length;i++) {
						sum+=cmds[i];
					}
					help.setHelp(sum);
					cmd.getChannel().sendMessage("New Help has saved. Using /help to show.").queue();
				}else if(cmds.length==1) {
					Help help = new Help(event.getGuild().getId());
					Embed.EmbedSender(Color.pink, cmd.getChannel(), ":regional_indicator_h::regional_indicator_e::regional_indicator_l::regional_indicator_p:", help.getHelp());
				}
			}
		}
	}
}
