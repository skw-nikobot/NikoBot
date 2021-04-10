package com.github.smallru8.NikoBot.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Help {
	
	private String serverID = "";
	
	public Help(String serverID) {
		this.serverID = serverID;
	}
	
	public void setHelp(String text) {
		try {
			FileWriter fw = new FileWriter("servers/" + serverID + "/help");
			fw.write(text + "\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getHelp() {
		String sum = "";
		try {
			FileReader fr = new FileReader("servers/" + serverID + "/help");
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while((line=br.readLine())!=null) {
				sum+=line;
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sum;
	}
}
