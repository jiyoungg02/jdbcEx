package db.item6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.util.DBConn;
import db.util.DBUtil;

/*
 - 결과 집합 유형
   TYPE_FORWARD_ONLY : 기본. 순방향만 이동 가능
   TYPE_SCROLL_SENSITIVE : 순방향 및 역방향 이동 가능. 수정 결과 바로 반영
   TYPE_SCROLL_INSENSITIVE : 순방향 및 역방향 이동 가능. 수정 결과 바로 반영 안됨
   
 - 동시성 유형
   CONCUR_READ_ONLY : 기본. 읽기만 가능
   CONCUR_UPDATABLE : 수정 가능
   
 - 쿼리 실행후 SQL Developer 등 외부에서 INSERT, UPDATE, DELETE 된 정보는
   rs를 close 하고 다시 쿼리를 실행해야 변경 가능하다.
   
 - 외부에서 UPDATE 된 정보는 TYPE_SCROLL_SENSITIVE 옵션을 사용하고
   rs.next() 다음에  rs.refreshRow()를 호출하면 변경된 정보를 확인할 수 있다.
*/

public class Ex01_Scroll {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		char ch;
		String sql;
		
		try {
			sql = "SELECT hak, name, birth, kor, eng, mat FROM score";
			
			// pstmt = conn.prepareStatement(sql); // 순방향만 가능(기본) : next() 만 가능
			
			// 순방향 및 역방향 모두 가능
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);
			
			rs = pstmt.executeQuery();
			
			while(true) {
				System.out.print("1.처음 2.이전 3.다음 4.마지막 5.종료 => ");
				ch = (char)System.in.read();
				System.in.skip(2);
				
				if(ch == '5') {
					break;
				}
				
				switch(ch){
				case '1' :
					if(rs.first()) {
						System.out.println("처음 -> " + rs.getString(1) 
							+ " : " + rs.getString(2));
					}
					break;
				case '2' :
					if(rs.previous()) {
						System.out.println("이전 -> " + rs.getString(1) 
							+ " : " + rs.getString(2));
					}
					break;
				case '3' :
					if(rs.next()) {
						rs.refreshRow();
						System.out.println("다음 -> " + rs.getString(1) 
							+ " : " + rs.getString(2));
					}
					break;
				case '4' :
					if(rs.last()) {
						System.out.println("마지막 -> " + rs.getString(1) 
							+ " : " + rs.getString(2));
					}
					break;
				}
			}
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
			
			DBConn.close();
		}
	}
}
