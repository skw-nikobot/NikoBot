package com.github.smallru8.NikoBot.event;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Event {
	
	/**
	 * 
	 * Event class
	 *
	 */
	public static class MessageEvent { 
		public Message msg;
		public MessageReceivedEvent event;
		public MessageEvent(MessageReceivedEvent event){
			msg = event.getMessage();
			this.event = event;
		}
	}
}
