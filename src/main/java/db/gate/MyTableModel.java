package db.gate;

import javax.swing.table.DefaultTableModel;

// JTable 모델 클래스
// DefaultTableModel : AbstractTableModel 추상클래스를 구현한 하위 클래스
public class MyTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 2L;

	protected String title[]; // 테이블 헤더

	public MyTableModel(String[] title) {
		this.title = title;
	}

	// 컬럼의 개수
	@Override
	public int getColumnCount() {
		if (title == null)
			return 0;

		return title.length;
	}

	// 컬럼의 헤더 제목
	@Override
	public String getColumnName(int column) {
		if (title == null || title.length == 0)
			return null;

		return title[column];
	}
}
