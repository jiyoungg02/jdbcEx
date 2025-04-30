package db.item3.score;

import java.sql.SQLException;
import java.util.List;

public interface ScoreDAO {
	public void insertScore(ScoreDTO dto) throws SQLException;
	public void updateScore(ScoreDTO dto) throws SQLException;
	public void deleteScore(String hak) throws SQLException;
	
	public ScoreDTO findById(String hak);
	public List<ScoreDTO> listScore();
	public List<ScoreDTO> listScore(String name);
}
