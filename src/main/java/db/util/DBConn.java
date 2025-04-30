package db.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
	private static Connection conn;
	
	private static final String URL = "jdbc:oracle:thin:@//127.0.0.1:1521/XE";
	private static final String USER = "sky";
	private static final String PASSWORD = "java$!";
	
	private DBConn() {
		
	}
	
	public static Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static void close() {
		if(conn != null) {
			try {
				if( ! conn.isClosed() ) {
					conn.close();
				}
			} catch (Exception e) {
			}
		}
		
		conn = null;
	}
}
