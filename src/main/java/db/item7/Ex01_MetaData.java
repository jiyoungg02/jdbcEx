package db.item7;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import db.util.DBConn;
import db.util.DBUtil;

public class Ex01_MetaData {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		String sql;
		
		try {
			sql = "SELECT * FROM score";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			rsmd = rs.getMetaData();
			
			// 컬럼수
			int cols = rsmd.getColumnCount();
			
			System.out.println("전체컬럼수 : " + cols);
			System.out.print("컬럼명\t\t\t");
			System.out.print("타입\t");
			System.out.print("타입명\t\t");
			System.out.println("컬럼폭");
			for(int i = 1; i <= cols; i++) {
				System.out.print(rsmd.getColumnClassName(i) + "\t");
				System.out.print(rsmd.getColumnType(i) + "\t"); // 컬럼 타입에 해당하는 숫자
				System.out.print(rsmd.getColumnTypeName(i) + "\t");
				System.out.println(rsmd.getPrecision(i));
			}
			System.out.println();
			
			// 데이터
			while(rs.next()) {
				for(int i = 1; i<=cols; i++) {
					System.out.print(rs.getString(i) + "\t");
				}
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
			
			DBConn.close();
		}
	}
}
