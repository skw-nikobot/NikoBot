package com.github.smallru8.NikoBot.event;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.greenrobot.eventbus.EventBus;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.StdOutput;
import com.github.smallru8.NikoBot.commands.Help;
import com.github.smallru8.NikoBot.commands.Info;
public class EventSender extends ListenerAdapter{

	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		/** Send msg to plugins*/
		if(!Core.sleepFlag)//send to normal plugins
			if(EventBus.getDefault().hasSubscriberForEvent(Event.MessageEvent.class))
				EventBus.getDefault().post(new Event.MessageEvent(event));
	}

	/**
	 * On bot join a guild
	 */
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		String guildID = event.getGuild().getId();
		StdOutput.infoPrintln("Join server : " + guildID);
		Core.ADMINS.addTable(guildID);
		Core.ADMINS.addAdmin(guildID, event.getGuild().getOwnerId());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Info info = new Info(guildID);
		info.setInfo("Using /info set <text> to set info.");
		Help help = new Help(guildID);
		help.setHelp("Using /help set <text> to set help.");
	}
	
	/**
	 * On bot leave a guild
	 */
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		StdOutput.infoPrintln("Leave server : " + event.getGuild().getId());
		Core.ADMINS.delTable(event.getGuild().getId());
		File f = new File("servers/"+event.getGuild().getId());
		f.delete();
	}
	
}
