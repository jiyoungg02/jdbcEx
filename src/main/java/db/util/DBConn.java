package db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// 싱글턴 패턴(Singleton Pattern)
public class DBConn {
	private static Connection conn;

	// private static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";  // 11g
	private static final String URL = "jdbc:oracle:thin:@//127.0.0.1:1521/XE"; // 12C 이상
	private static final String USER = "sky";
	private static final String PASSWORD = "java$!";

	private DBConn() {
	}

	public static Connection getConnection() {
		if(conn == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}

	public static Connection getConnection(String url, String user, String pwd, String internal_logon) {
		if (conn == null) {
			try {
				Properties info = new Properties();
				info.put("user", user);
				info.put("password", pwd);
				info.put("internal_logon", internal_logon); // sysdba 같은 롤
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, info);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		return conn;
	}

	public static void close() {
		if (conn != null) {
			try {
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
			}
		}

		conn = null;
	}
}
