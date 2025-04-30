package db.item2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import db.util.DBConn;

public class Ex02_ResultSet {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			// ResultSet : SELECT 문의 결과를 가지고 있는 객체
			// 쿼리 작성시 주의 사항
			// : 쿼리를 작성할때 쿼리 마지막에 ;를 붙이지 않는다.
			// : 쿼리 마지막에 ; 이 있으면 ORA-00933 에러 발생
			
			stmt = conn.createStatement();
			
			sql = "SELECT hak hno, name, birth, kor, eng, mat, "
					+ " kor+eng+mat tot "
					+ " FROM score "
					+ " WHERE hak = '1234'";
			
			// executeQuery() 
			// : SELECT 문을 실행하고 결과를 가지고 있는 ResultSet 객체 반환
			
			rs = stmt.executeQuery(sql);
			
			// next() : 커서를 다음행으로 이동하고, 더이상 행이 없으면 false 반환
			if(rs.next()) {
				// String hak = rs.getString("hak"); //에러
					// 쿼리에서 컬럼명을 변경하면 변경된 이름으로 가져와야 함
				String hak = rs.getString("HNO"); // 대소문자 구분 안함
				// String hak = rs.getString(1); // 1번째 컬럼값 반환
				
				String name = rs.getString("name");
				// String birth = rs.getString("birth");
					// YYYY-MM-DD HH24:MI:SS 로 반환
				String birth = rs.getDate("birth").toString(); 
					// YYYY-MM-DD 로 반환
					// rs.getDate() : java.sql.Date를 반환
				
				int kor = rs.getInt("kor");
				// String kor = rs.getString("kor"); // 문자로도 가져올 수 있다.
				int eng = rs.getInt("eng");
				int mat = rs.getInt("mat");
				int tot = rs.getInt("tot");
				
				System.out.print(hak + "\t");
				System.out.print(name + "\t");
				System.out.print(birth + "\t");
				System.out.print(kor + "\t");
				System.out.print(eng + "\t");
				System.out.print(mat + "\t");
				System.out.println(tot);
			}
			
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
