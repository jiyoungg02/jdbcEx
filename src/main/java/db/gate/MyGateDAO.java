package db.gate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBUtil;

public class MyGateDAO {
	private Connection conn = null;
	private String[] columnNames;

	public MyGateDAO(Connection conn) {
		this.conn = conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	// SELECT
	public List<String[]> getResultList(String sql) throws SQLException, Exception {
		List<String[]> list = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData resultMetaData = null;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			resultMetaData = rs.getMetaData(); // 메타정보

			// 필드 수 구하기
			int cols = resultMetaData.getColumnCount();

			// 필드명 구하기
			columnNames = new String[cols];
			for (int i = 0; i < cols; i++) {
				columnNames[i] = resultMetaData.getColumnName(i + 1);
			}

			// 결과
			list = new ArrayList<String[]>();
			while (rs.next()) {
				String[] results = new String[cols];

				for (int i = 0; i < cols; i++) {
					results[i] = rs.getString(i + 1);
				}

				list.add(results);
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();

			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	public List<String[]> getDescList(String sql) throws SQLException, Exception {
		List<String[]> list = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData resultMetaData = null;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			resultMetaData = rs.getMetaData(); // 메타정보

			list = new ArrayList<String[]>();

			// 필드 수 구하기
			int cols = resultMetaData.getColumnCount();
			for (int i = 0; i < cols; i++) {
				String[] results = new String[3];

				// 컬럼명
				results[0] = resultMetaData.getColumnName(i + 1);
				if (resultMetaData.isNullable(i + 1) == 1)
					results[1] = "(null)";
				else
					results[1] = "NOT NULL";

				int size = resultMetaData.getPrecision(i + 1);
				if (size == 0)
					results[2] = resultMetaData.getColumnTypeName(i + 1);
				else
					results[2] = resultMetaData.getColumnTypeName(i + 1) + "(" + size + ")";
				list.add(results);
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();

			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	public String execute(String sql) throws SQLException, Exception {
		String message = null;
		int result = 0;

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();

			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

		if (sql.toLowerCase().startsWith("insert")) {
			message = result + " 개의 데이터가 추가 되었습니다.";
		} else if (sql.toLowerCase().startsWith("update")) {
			message = result + " 개의 데이터가 수정 되었습니다.";
		} else if (sql.toLowerCase().startsWith("detete")) {
			message = result + " 개의 데이터가 삭제 되었습니다.";
		} else {
			message = " 쿼리가 정상적으로 실행 되었습니다.";
		}

		return message;
	}

	public String[] getColumnNames() {
		return columnNames;
	}
}
