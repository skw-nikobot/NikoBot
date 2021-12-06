package com.github.smallru8.NikoBot.SQL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.github.smallru8.NikoBot.StdOutput;

public class SQL {
	
	/**
	 * True : MySQL/Mariadb
	 * False : SQLite
	 */
	private boolean useMySQL = false;
	private String host = "jdbc:sqlite:SQL/NikoBot.db";
	private String name = "root";
	private String passwd = "passwd";
	
	public SQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName("org.sqlite.JDBC");
			//Load sql config
			Properties pro = new Properties();
			InputStream is = new FileInputStream("SQL/sql.conf");
			pro.load(is);
			if(pro.getProperty("mySQL", "false").equalsIgnoreCase("true")) {
				useMySQL = true;
				host = "jdbc:mysql://"+pro.getProperty("host", "127.0.0.1")+":"+pro.getProperty("port", "3306")+"/"+pro.getProperty("db", "NikoBot");
				name = pro.getProperty("username", "root");
				passwd = pro.getProperty("passwd", "passwd");
			}
			is.close();
		} catch (ClassNotFoundException e1) {
			StdOutput.errorPrintln("SQLite driver not found.");
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connect to SQL server/SQLite
	 * @return
	 */
	protected Connection getSQLConnection() {
		Connection conn = null;
		try {
			if(useMySQL)
				conn = DriverManager.getConnection(host,name,passwd);
			else {
				conn = DriverManager.getConnection(host);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
