package db.item4.member;

import java.sql.SQLException;
import java.util.List;

public interface MemberDAO {
	public int insertMember(MemberDTO dto) throws SQLException;
	public int updateMember(MemberDTO dto) throws SQLException;
	public int deleteMember(String id) throws SQLException;
	
	public MemberDTO findById(String id);
	public List<MemberDTO> listMember();
	public List<MemberDTO> listMember(String name);	
}
