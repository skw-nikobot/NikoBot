package com.github.smallru8.NikoBot.event;

import org.greenrobot.eventbus.EventBus;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.event.Event;

public class EventSender extends ListenerAdapter{

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		
		/** Send msg to plugins*/
		if(!Core.sleepFlag)//send to normal plugins
			if(EventBus.getDefault().hasSubscriberForEvent(Event.MessageEvent.class))
				EventBus.getDefault().post(new Event.MessageEvent(event));
		
	}
	
}
