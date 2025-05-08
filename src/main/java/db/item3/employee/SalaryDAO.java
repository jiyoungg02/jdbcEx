package db.item3.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			sql="INSERT INTO salary(salaryNum, sabeon, payDate, paymentDate, pay, sudang, tax, memo) "
			       + " VALUES (salary_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSabeon());
			pstmt.setString(2, dto.getPayDate());
			pstmt.setString(3, dto.getPaymentDate());
			pstmt.setInt(4, dto.getPay());
			pstmt.setInt(5, dto.getSudang());
			pstmt.setInt(6, dto.getTax());
			pstmt.setString(7, dto.getMemo());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	
	public int updateSalary(SalaryDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE salary SET sabeon=?, payDate=?, paymentDate=?, pay=?, sudang=?, tax=?, memo=? "
			      + " WHERE salaryNum=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSabeon());
			pstmt.setString(2, dto.getPayDate());
			pstmt.setString(3, dto.getPaymentDate());
			pstmt.setInt(4, dto.getPay());
			pstmt.setInt(5, dto.getSudang());
			pstmt.setInt(6, dto.getTax());
			pstmt.setString(7, dto.getMemo());
			pstmt.setInt(8, dto.getSalaryNum());
			
			result= pstmt.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	public int deleteSalary(int salaryNum) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM salary WHERE salaryNum=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, salaryNum);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	public SalaryDTO readSalary(int salaryNum) {
		SalaryDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT salaryNum, s.sabeon, name, payDate, ");
			sb.append("    TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate, ");
			sb.append("    pay, sudang, pay+sudang tot, tax, (pay+sudang)-tax afterPay, memo ");
			sb.append(" FROM salary s ");
			sb.append(" JOIN employee e ON s.sabeon = e.sabeon ");
			sb.append(" WHERE salaryNum = ?");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, salaryNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTot(rs.getInt("tot"));
				dto.setTax(rs.getInt("tax"));
				dto.setAfterPay(rs.getInt("afterPay"));
				dto.setMemo(rs.getString("memo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public List<SalaryDTO> listSalary(String payDate) {
		List<SalaryDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT salaryNum, s.sabeon, name, payDate, ");
			sb.append("    TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate, ");
			sb.append("    pay, sudang, pay+sudang tot, tax, (pay+sudang)-tax afterPay, memo ");
			sb.append(" FROM salary s ");
			sb.append(" JOIN employee e ON s.sabeon = e.sabeon ");
			sb.append(" WHERE payDate = ?");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, payDate);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SalaryDTO dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTot(rs.getInt("tot"));
				dto.setTax(rs.getInt("tax"));
				dto.setAfterPay(rs.getInt("afterPay"));
				dto.setMemo(rs.getString("memo"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	public List<SalaryDTO> listSalary(Map<String, Object> map) {
		List<SalaryDTO> list = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			String sabeon = (String)map.get("sabeon");
			String payDate = (String)map.get("payDate");
			
			sb.append(" SELECT salaryNum, s.sabeon, name, payDate, ");
			sb.append("   TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate, ");
			sb.append("   pay, sudang, pay+sudang tot, tax, (pay+sudang)-tax afterPay, memo ");
			sb.append(" FROM salary s ");
			sb.append(" JOIN employee e ON s.sabeon = e.sabeon ");
			sb.append(" WHERE s.sabeon = ? AND payDate = ?");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, sabeon);
			pstmt.setString(2, payDate);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SalaryDTO dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTot(rs.getInt("tot"));
				dto.setTax(rs.getInt("tax"));
				dto.setAfterPay(rs.getInt("afterPay"));
				dto.setMemo(rs.getString("memo"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}

	public List<SalaryDTO> listSalary() {
		List<SalaryDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT salaryNum, s.sabeon, name, payDate, ");
			sb.append("    TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate, ");
			sb.append("    pay, sudang, pay+sudang tot, tax, (pay+sudang)-tax afterPay, memo ");
			sb.append(" FROM salary s ");
			sb.append(" JOIN employee e ON s.sabeon = e.sabeon ");
			sb.append(" ORDER BY payDate DESC ");
			pstmt = conn.prepareStatement(sb.toString());
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				SalaryDTO dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTot(rs.getInt("tot"));
				dto.setTax(rs.getInt("tax"));
				dto.setAfterPay(rs.getInt("afterPay"));
				dto.setMemo(rs.getString("memo"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}

}
