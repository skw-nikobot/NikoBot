package com.github.smallru8.NikoBot;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;


public class Embed {
	
	/**
	 * Send an embed message.
	 * @param c embed color.
	 * @param ch channel.
	 * @param emo message line one.
	 * @param str others message.
	 */
	public static void EmbedSender(Color c,MessageChannelUnion ch,String emo,String str) {
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(c);
	    embed.setTitle(emo);
	    embed.setDescription(str);
	    ch.sendMessageEmbeds(embed.build()).queue();
	}
	
}
