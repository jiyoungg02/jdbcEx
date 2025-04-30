package db.item2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.util.DBConn;

/*
  - Statement
    : 정적인 SQL 문을 실행하기 위한 메소드를 제공하는 인터페이스
    : Connection 의 createStatement() 메소드를 이용하여 객체를 반환 받는다.
    : SQL Injection(SQL 주입) 공격이 가능하므로 절대 사용하지 않는다.
*/

public class Ex01_Statement {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		String sql;
		int result;
		
		try {
			// 두번 실행하면 기본키 제약 위반으로 ORA-00001이 발생
			// 자바에서 INSERT, UPDATE, DELETE 하면 기본적으로 COMMIT 된다.
			
			stmt = conn.createStatement();
			
			sql = "INSERT INTO score(hak, name, birth, kor, eng, mat) " 
					+ "VALUES ('1235', '이자바', TO_DATE('2010-10-10', 'YYYY-MM-DD'), "
					+ " 85, 90, 75)";
			
			// executeUpdate()
			// : DML(INSERT, UPDATE, DELETE) 실행 후 변경된 행수 반환
			// : DDL(CREATE, ALTER, DROP) 실행
			result = stmt.executeUpdate(sql);
			
			System.out.println(result + "행이 추가 되었습니다.");
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
