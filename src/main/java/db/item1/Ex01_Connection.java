package db.item1;

import java.sql.Connection;

import db.util.DBConn;

public class Ex01_Connection {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		
		System.out.println(conn);
	}
}
