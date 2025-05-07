package db.item4.score;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ScoreDAO {
	public int insertScore(ScoreDTO dto) throws SQLException;
	public int updateScore(ScoreDTO dto) throws SQLException;
	public int deleteScore(String hak) throws SQLException;
	
	public ScoreDTO findById(String hak);
	public List<ScoreDTO> listScore();
	public List<ScoreDTO> listScore(String name);
	
	public Map<String, Integer> averageScore();
}
