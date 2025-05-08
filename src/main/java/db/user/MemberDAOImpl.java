package db.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

// PreparedStatement를 이용하여 db에 INSERT, UPDATE, DELETE, SELECT
/*
- 하나의 테이블에 추가
   INSERT INTO 테이블명(컬럼명, 컬럼명) VALUES (값, 값)
- 두개의 테이블에 추가
   INSERT ALL
     INTO 테이블명(컬럼명, 컬럼명) VALUES (값, 값)
     INTO 테이블명(컬럼명, 컬럼명) VALUES (값, 값)
     SELECT * FROM dual 
*/
public class MemberDAOImpl implements MemberDAO {
	private Connection conn = DBConn.getConnection();

	@Override
	public void insertMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		// member1과 member2 테이블에 추가
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO member1(id, name, pwd) VALUES (?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPwd());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			sql = "INSERT INTO member2(id, birth, email, tel) VALUES (?,TO_DATE(?, 'YYYY-MM-DD'),?,?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getTel());

			pstmt.executeUpdate();

/*
			sql = "INSERT ALL INTO member1(id,pwd,name) VALUES(?,?,?) "
			       + " INTO member2(id,birth,email,tel) VALUES(?,TO_DATE(?, 'YYYY-MM-DD'),?,?) ";
			       + " SELECT * FROM dual";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getId());
			pstmt.setString(5, dto.getBirth());
			pstmt.setString(6, dto.getEmail());
			pstmt.setString(7, dto.getTel());
			pstmt.executeUpdate();
 */
			conn.commit();
		} catch (SQLException e) {
			// e.printStackTrace();
			
			DBUtil.rollback(conn);
			
			throw e;
		} finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}

	@Override
	public void updateMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		// UPDATE 테이블명 SET 컬럼=값, 컬럼=값 WHERE 조건
		
		// id 조건에 맞는 member1 테이블과 member2 테이블 데이터 수정
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE member1 SET pwd=? WHERE id=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPwd());
			pstmt.setString(2, dto.getId());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			sql = "UPDATE member2 SET birth=TO_DATE(?, 'YYYY-MM-DD'), email=?, tel=? WHERE id=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, dto.getId());

			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			// e.printStackTrace();
			DBUtil.rollback(conn);
			
			throw e;
		} finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}

	@Override
	public void deleteMember(String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		// DELETE FROM 테이블명 WHERE 조건
		
		// id 조건에 맞는 member1 테이블과 member2 테이블 데이터 삭제
		try {
			conn.setAutoCommit(false);
			
			sql = "DELETE FROM member2 WHERE id=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			sql = "DELETE FROM member1 WHERE id=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, id);

			pstmt.executeUpdate();

			conn.commit();
			
		} catch (SQLException e) {
			// e.printStackTrace();
			
			DBUtil.rollback(conn);
			
			throw e;
		} finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}

	@Override
	public MemberDTO findById(String id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		/*
		- 하나의 테이블
		  SELECT 컬럼, 컬럼 FROM 테이블 WHERE 조건
		- EQUI 조인
		  SELECT 테이블1.컬럼, 테이블1.컬럼, 테이블2.컬럼
		  FROM 테이블1
		  JOIN 테이블2 ON 테이블1.컬럼 = 테이블2.컬럼
		  WHERE 조건
		- LEFT OUTER JOIN 조인
		  SELECT 테이블1.컬럼, 테이블1.컬럼, 테이블2.컬럼
		  FROM 테이블1
		  LEFT OUTER JOIN 테이블2 ON 테이블1.컬럼 = 테이블2.컬럼
		  WHERE 조건
		 */
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터를 OUTER JOIN 해서 아이디 검색
		try {
			sql = "SELECT m1.id, pwd, name, TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel "
					+ " FROM member1 m1 "
					+ " LEFT OUTER JOIN member2 m2 ON m1.id = m2.id"
					+ " WHERE m1.id=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();

				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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
		List<MemberDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		// member1과 member2를 id를 기준으로 LEFT OUTER JOIN 하여 전체 레코드 반환
		
		try {
			sql = "SELECT m1.id, pwd, name, TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel "
					+ " FROM member1 m1 "
					+ " LEFT OUTER JOIN member2 m2 ON m1.id = m2.id";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();

				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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
		List<MemberDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 이름 검색
		
		try {
			sql = "SELECT m1.id, pwd, name, TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel "
					+ " FROM member1 m1 "
					+ " LEFT OUTER JOIN member2 m2 ON m1.id = m2.id"
					+ " WHERE INSTR(name, ?)>=1";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();

				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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
