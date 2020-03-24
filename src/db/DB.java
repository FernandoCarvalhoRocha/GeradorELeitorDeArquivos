package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conn = null;
	
	private static Properties LoadProperties() {
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;	
		}
		catch (IOException e) {
			throw new DbExeception(e.getMessage());
			
		}
	}
	
	public static Connection getConnection() {
		try {
			if (conn == null) {
				Properties props = LoadProperties();
				String 	url = props.getProperty("dburl");	
				conn = DriverManager.getConnection(url, props);
				}
			}
		catch (SQLException e) {
			throw new DbExeception(e.getMessage());
		}
		return conn;
	}
	
	public static void closeConnection() {
		if (conn != null) {
			try {
			conn.close();
			}
			catch (SQLException e) {
				throw new DbExeception(e.getMessage());
			}
		}
			
	}
	
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
			st.close();
			}
			catch (SQLException e) {
				throw new DbExeception(e.getMessage());
			}
		}
			
	}

}