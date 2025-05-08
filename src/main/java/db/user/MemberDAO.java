package db.user;

import java.sql.SQLException;
import java.util.List;

public interface MemberDAO {
	public void insertMember(MemberDTO dto) throws SQLException;
	public void updateMember(MemberDTO dto) throws SQLException;
	public void deleteMember(String id) throws SQLException;
	
	public MemberDTO findById(String id);
	public List<MemberDTO> listMember();
	public List<MemberDTO> listMember(String name);	
}
