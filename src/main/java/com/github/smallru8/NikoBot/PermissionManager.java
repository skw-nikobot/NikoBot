package com.github.smallru8.NikoBot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Message;

public class PermissionManager {
	
	public ArrayList<String> adminLs;
	/**
	 * Read admin list from file.
	 */
	PermissionManager(){
		adminLs = new ArrayList<String>();
		FileReader frUidLs;
		try {
			frUidLs = new FileReader("cfg/admin");
			BufferedReader br = new BufferedReader(frUidLs);
			String tmp = null;
			while((tmp = br.readLine())!=null)
				adminLs.add(tmp);
			br.close();
			frUidLs.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * Check the user id is in admin list.
	 * @param msg
	 * @return return true while user is admin, others return false.  
	 */
	public boolean userId(Message msg) {
		
		String usrId = msg.getAuthor().getId();
		for(String ele:adminLs) {
			if(usrId.equals(ele))
				return true;
		}
		
		Embed.EmbedSender(Color.pink,msg.getChannel(),":no_entry_sign:", "你不是我的主人，尼奏開!!! You don't have permission to do this!");
		return false;
	}
	
}
