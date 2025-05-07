package db.item5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.util.DBConn;
import db.util.DBUtil;

// Transaction : DB의 상태를 변화시키는 하나의 논리적 기능을 수행하기 위한 작업의 단위
public class Ex01_Transaction {
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		
		String sql;
		String id, name, birth, tel;
		
		try {
			System.out.print("아이디 ? ");
			id = br.readLine();
			
			System.out.print("이름 ? ");
			name = br.readLine();
			
			System.out.print("생년월일 ? ");
			birth = br.readLine();
			
			System.out.print("전화번호 ? ");
			tel = br.readLine();
			
			// 자동으로 COMMIT 되지 않도록 설정
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO test1(id, name) VALUES(?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO test2(id, birth) VALUES(?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, birth);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO test3(id, tel) VALUES(?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, tel);
			pstmt.executeUpdate();
			
			conn.commit(); // 커밋
			
			System.out.println("데이터 추가 성공...");
			
		} catch (SQLException e) {
			try {
				conn.rollback(); // 예외가 발생하면 롤백
			} catch (Exception e2) {
			}
			
			System.out.println("데이터 추가 실패...");
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			
			DBUtil.close(pstmt);
			DBConn.close();
		}
		
	}
}
