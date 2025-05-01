package db.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.util.DBConn;
import db.util.DBUtil;

public class SalaryDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertSalary(SalaryDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO salary(salaryNum, sabeon, payDate, paymentDate, pay, sudang, tax, memo)"
					+ " VALUES(?, ?, ?, TO_DATE(?, 'YYYYMMDD'), ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getSalaryNum());
			pstmt.setString(2, dto.getSabeon());
			pstmt.setString(3, dto.getPayDate());
			pstmt.setString(4, dto.getPaymentDate());
			pstmt.setInt(5, dto.getPay());
			pstmt.setInt(6, dto.getSudang());
			pstmt.setInt(7, dto.getTax());
			pstmt.setString(8, dto.getMemo());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	
	public int updateSalary(SalaryDTO dto) throws SQLException {
		int result = 0;
		
		return result;
	}

	public int deleteSalary(int salaryNum) throws SQLException {
		int result = 0;

		return result;
	}
	
	public SalaryDTO readSalary(int salaryNum) {
		SalaryDTO dto = null;
		
		return dto;
	}
	
	public List<SalaryDTO> listSalary(String payDate) {
		List<SalaryDTO> list = new ArrayList<>();
		
		return list;
	}
	
	public List<SalaryDTO> listSalary(Map<String, Object> map) {
		List<SalaryDTO> list = new ArrayList<>();
		
		return list;
	}

	public List<SalaryDTO> listSalary() {
		List<SalaryDTO> list = new ArrayList<>();
		
		return list;
	}

}
