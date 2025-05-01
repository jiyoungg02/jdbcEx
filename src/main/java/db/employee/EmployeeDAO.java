package db.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class EmployeeDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertEmployee(EmployeeDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO employee(sabeon, name, birth, tel) "
					+ " VALUES(?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSabeon());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getBirth());
			pstmt.setString(4, dto.getTel());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	
	public int updateEmployee(EmployeeDTO dto) throws SQLException {
		int result = 0;
		
		return result;
	}
	
	public EmployeeDTO readEmployee(String sabeon) {
		EmployeeDTO dto = null;

		return dto;
	}
	
	public List<EmployeeDTO> listEmployee() {
		List<EmployeeDTO> list = new ArrayList<>();
		
		return list;
	}
	
	public List<EmployeeDTO> listEmployee(String name) {
		List<EmployeeDTO> list = new ArrayList<>();
		
		return list;
	}
}
