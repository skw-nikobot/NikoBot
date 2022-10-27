package com.github.smallru8.NikoBot.SQL;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.smallru8.NikoBot.Embed;
import com.github.smallru8.NikoBot.StdOutput;

import net.dv8tion.jda.api.entities.Message;

/**
 * Table: ADMINS*
 * Field: |UID|
 * @author smallru8
 *
 */
public class AdminData extends SQL{
	
	public AdminData() {
		super();
		//Global admin
		String sql = "CREATE TABLE ADMINS (UID VARCHAR(24) not NULL, PRIMARY KEY (UID));"; 
		Connection conn = getSQLConnection();
		try {
			//TODO Fix bug SQLite conn exception
			if (!isTableExist("ADMINS")) {//Table not exist. Create table
				PreparedStatement ps = conn.prepareStatement(sql);
				int ret = ps.executeUpdate();
				ps.close();
				if(ret > 0)
					StdOutput.infoPrintln("[SQL]Create table: ADMINS");
				else
					StdOutput.errorPrintln("[SQL]Can't create table: ADMINS");
			}
			/*Default global admin*/
			FileReader fr = new FileReader("conf.d/admin");
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while((line=br.readLine())!=null) {
				addAdmin("",line);
			}
			br.close();
			fr.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isTableExist(String tableName) {
		Connection conn = getSQLConnection();
		try {
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, tableName, null);
			conn.close();
			if (tables.next()) {//Table exist.
				return true;
			}
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * While bot join server, Call this mathod
	 * @param serverID
	 */
	public void addTable(String serverID) {
		if(!isTableExist("ADMINS"+serverID)) {
			String sql = "CREATE TABLE ADMINS" + serverID + " (UID VARCHAR(24) not NULL, PRIMARY KEY (UID));";
			Connection conn = getSQLConnection();
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				int ret = ps.executeUpdate();
				ps.close();
				conn.close();
				if(ret > 0)
					StdOutput.infoPrintln("[SQL]Create table: ADMINS"+serverID);
				else
					StdOutput.errorPrintln("[SQL]Can't create table: ADMINS"+serverID);
			} catch (SQLException e) {
				e.getMessage();
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * While bot leave server, Call this mathod
	 * @param serverID
	 */
	public void delTable(String serverID) {
		if(isTableExist("ADMINS"+serverID)) {
			String sql = "DROP TABLE ADMINS" + serverID + ";";
			Connection conn = getSQLConnection();
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				int ret = ps.executeUpdate();
				ps.close();
				conn.close();
				if(ret > 0)
					StdOutput.infoPrintln("[SQL]Delete table: ADMINS"+serverID);
				else
					StdOutput.errorPrintln("[SQL]Can't delete table: ADMINS"+serverID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Add admin. If serverID = "", the user will become global admin.
	 * @param serverID
	 * @param userID
	 */
	public boolean addAdmin(String serverID,String userID) {
		Connection conn = getSQLConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ADMINS"+ serverID +" WHERE UID = ?;");
			ps.setString(1, userID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//UID重複
				rs.close();
				ps.close();
				conn.close();
				return false;
			}
			rs.close();
			ps.close();
			ps = conn.prepareStatement("INSERT INTO ADMINS"+ serverID +" (UID)\n" + "VALUES (?);");
			ps.setString(1, userID);
			int ret = ps.executeUpdate();
			ps.close();
			conn.close();
			if(ret > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Delete admin. If serverID = "", it will delete a global admin.
	 * @param serverID
	 * @param userID
	 * @return
	 */
	public boolean delAdmin(String serverID,String userID) {
		Connection conn = getSQLConnection();
		try {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM ADMINS"+serverID+" WHERE UID = ?;");
		ps.setString(1, userID);
		int ret = ps.executeUpdate();
		ps.close();
		conn.close();
		if(ret > 0)
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * If serverID = "", it will only check whether the user is a global admin
	 * Auto reply
	 * @param serverID
	 * @param msg
	 * @return
	 */
	public boolean isAdmin(String serverID,Message msg) {
		String userID = msg.getAuthor().getId();
		boolean ret = isAdmin(serverID,userID);
		if(!ret)
			Embed.EmbedSender(Color.pink,msg.getChannel(),":no_entry_sign:", "You don't have permission to do this!");
		return ret;
	}
	
	/**
	 * If serverID = "", it will only check whether the user is a global admin
	 * @param serverID
	 * @param userID
	 * @return
	 */
	public boolean isAdmin(String serverID,String userID) {
		Connection conn = getSQLConnection();
		boolean ret = false;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT UID FROM ADMINS"+serverID+" WHERE UID = ?;");
			ps.setString(1, userID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				ret =  true;
			}
			rs.close();
			ps.close();
			
			if(serverID!="") {//Don't have local admin but have global admin
				ps = conn.prepareStatement("SELECT UID FROM ADMINS" + " WHERE UID = ?;");
				ps.setString(1, userID);
				rs = ps.executeQuery();
				if(rs.next()) {
					ret =  true;
				}
				rs.close();
				ps.close();
			}
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
}
