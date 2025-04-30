package db.item2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.util.DBConn;

public class Ex03_SQLInjection {
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql;
		String kwd;
		
		String hak, name, birth;
		int kor, eng, mat;
		
		try {
			System.out.print("검색할 학번 ? ");
			kwd = br.readLine();
			
			sql = "SELECT hak, name, birth, kor, eng, mat "
					+ " FROM score "
					+ " WHERE hak = '" + kwd + "'";
			
			/*
			  - 검색할 학번을
			    1' OR name IS NOT NULL OR name = '이 
			    처럼 입력하면
			    WHERE hak = '1' OR name IS NOT NULL OR name = '이'
			    처럼 조건이 만들어지므로 모든 레코드가 출력 된다.
			    => SQL Injection
			    따라서 Statement 를 사용하면 안된다.
			*/
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				hak = rs.getString("hak");
				name = rs.getString("name");
				birth = rs.getString("birth");
				
				kor = rs.getInt("kor");
				eng = rs.getInt("eng");
				mat = rs.getInt("mat");
				
				System.out.print(hak + "\t");
				System.out.print(name + "\t");
				System.out.print(birth + "\t");
				System.out.print(kor + "\t");
				System.out.print(eng + "\t");
				System.out.println(mat);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(stmt != null) {
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		DBConn.close();
	}
}
