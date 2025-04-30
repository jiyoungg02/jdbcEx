package db.item3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.util.DBConn;

/*
  - PreparedStatement
    : Statement 하위 인터페이스
    : 전처리된 SQL 문을 나타내는 객체
*/
public class Ex01_PreparedStatement {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		int result;
		
		try {
			sql = "INSERT INTO score(hak, name, birth, kor, eng, mat) "
					+ " VALUES(?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, "2001");
			pstmt.setString(2, "스프링");
			pstmt.setString(3, "2010-09-09");
			pstmt.setInt(4, 80);
			pstmt.setInt(5, 85);
			pstmt.setInt(6, 80);
			
			result = pstmt.executeUpdate(); // 쿼리 실행
						// executeUpdate() 메소드의 인자에 쿼리를 넣지 않는다.
			
			pstmt.setString(1, "2002");
			pstmt.setString(2, "오라클");
			pstmt.setString(3, "2010-08-09");
			pstmt.setInt(4, 70);
			pstmt.setInt(5, 70);
			pstmt.setInt(6, 75);
			
			result += pstmt.executeUpdate();
			
			System.out.println(result + "행이 추가되었습니다.");
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		DBConn.close();
	}
}
