package com.github.smallru8.NikoBot.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Info {
	
	private String serverID = "";
	
	public Info(String serverID) {
		this.serverID = serverID;
	}
	
	public boolean hasBeenSet() {
		return new File("servers/"+serverID+"/info").exists();
	}
	
	public void setInfo(String text) {
		try {
			FileWriter fw = new FileWriter("servers/" + serverID + "/info");
			fw.write(text + "\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getInfo() {
		String sum = "";
		try {
			FileReader fr = new FileReader("servers/" + serverID + "/info");
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
