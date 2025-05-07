package db.pscore;

import java.sql.SQLException;
import java.util.List;

public interface ScoreDAO {
	public int insertScore(ScoreDTO dto) throws SQLException;
	public int updateScore(ScoreDTO dto) throws SQLException;
	public int deleteScore(String hak) throws SQLException;
	
	public ScoreDTO findById(String hak);
	public List<ScoreDTO> listScore();
	public List<ScoreDTO> listScore(String name);
}
