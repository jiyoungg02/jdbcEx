package db.item7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import db.util.DBConn;
import db.util.DBUtil;

public class Sqlplus {
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String sql, s;
		int n;
		
		gogo:
		while(true) {
			try {
				System.out.print("SQL> ");
				sql = "";
				n = 1;
				do {
					s = br.readLine();
				
					if(s == null || s.equalsIgnoreCase("exit")) {
						DBConn.close();
						System.exit(0);
					}
					
					s = s.trim();
					sql += s + "  ";
					
					if(sql.trim().length() == 0) {
						continue gogo;
					}
					
					if(s.lastIndexOf(";") != s.length()-1) {
						System.out.print((++n) + " ");
					}
					
				} while(s.lastIndexOf(";") != s.length()-1);
				
				sql = sql.trim();
				sql = sql.substring(0, sql.length()-1); // 마지막 ; 제거
				
				if(sql.length() == 0) {
					continue;
				}
				
				execute(sql);
				
			} catch(Exception e) {
				
			}
		}
	}
	
	public static void execute(String sql) {
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			if(sql.toUpperCase().startsWith("SELECT")) {
				rs = pstmt.executeQuery();
				rsmd = rs.getMetaData();
				
				int cols = rsmd.getColumnCount();
				for(int i=1; i<=cols; i++) {
					System.out.print(rsmd.getColumnName(i) + "\t");
				}
				System.out.println();
				for(int i=1; i<=cols; i++) {
					System.out.print("------------");
				}
				System.out.println();
				
				while(rs.next()) {
					for(int i=1; i<=cols; i++) {
						System.out.print(rs.getString(i) + "\t");
					}
					System.out.println();
				}
				
			} else {
				int result = pstmt.executeUpdate();
				
				if(sql.toUpperCase().startsWith("INSERT")) {
					System.out.println(result + " 행이 추가 되었습니다.");
				}else if(sql.toUpperCase().startsWith("UPDATE")){
					System.out.println(result + " 행이 수정 되었습니다.");
				}else if(sql.toUpperCase().startsWith("DELETE")){
					System.out.println(result + " 행이 삭제 되었습니다.");
				} else {
					System.out.println("쿼리가 실행되었습니다.");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}
}
