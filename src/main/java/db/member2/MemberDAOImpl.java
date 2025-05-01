package db.member2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class MemberDAOImpl implements MemberDAO{
	private Connection conn = DBConn.getConnection();

	@Override
	public int insertMember(MemberDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO member1(id, pwd, name) "
					+ " VALUES(?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			
			result = pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			pstmt = null;
			
			sql = "INSERT INTO member2(id, birth, email, tel) "
					+ " VALUES(?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getTel());
			
			result += pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}

	@Override
	public int updateMember(MemberDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "UPDATE member1 SET  pwd = ?, name = ? "
					+ " WHERE id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getPwd());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getId());
			
			result = pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			pstmt = null;
			
			sql = "UPDATE member2 SET birth = TO_DATE(?, 'YYYY-MM-DD'), email = ?, tel = ? "
					+ " WHERE id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, dto.getId());
			
			result += pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	@Override
	public int deleteMember(String id) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM member2 WHERE id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			
			
			sql = "DELETE FROM member1 WHERE id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	@Override
	public MemberDTO readMember(String id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT m1.id, name, pwd, birth, email, tel "
					+ " FROM member1 m1"
					+ " JOIN member2 m2 ON m1.id = m2.id "
					+ " WHERE m1.id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("pwd"));
				dto.setBirth(rs.getDate("birth").toString());
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}

	@Override
	public List<MemberDTO> listMember() {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT m1.id, name, pwd, birth, email, tel "
					+ " FROM member1 m1"
					+ " LEFT OUTER JOIN member2 m2 ON m1.id = m2.id "
					+ " ORDER BY m1.id";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("pwd"));
				dto.setBirth(rs.getDate("birth") !=null ? rs.getDate("birth").toString() : "");
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				
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

	@Override
	public List<MemberDTO> listMember(String name) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT m1.id, name, pwd, birth, email, tel "
					+ " FROM member1 m1"
					+ " JOIN member2 m2 ON m1.id = m2.id "
					+ " WHERE INSTR(name, ?) >= 1";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("pwd"));
				dto.setBirth(rs.getDate("birth").toString());
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				
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
