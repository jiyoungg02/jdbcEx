package db.pscore;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

// 테이블의 모델을 형성하기 위한 클래스(추가, 삭제, 개수확인 등을 지원)
public class MyTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	
	// 테이블 헤더의 타이틀
	private String[] title;
	
	public MyTableModel(String[] title) {
		this.title=title;
	}

	@Override
	public int getColumnCount() {
		// 컬럼수(열수)
		if(title==null)
			return 0;
		
		return title.length;
	}

	@Override
	public String getColumnName(int column) {
		// 컬럼명
		return title[column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// 더블클릭시 편집 못하게
		return false;
	}
}

class MyTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		
		if(column==0)
			setHorizontalAlignment(SwingConstants.LEFT);
		else if(column==1 || column==2)
			setHorizontalAlignment(SwingConstants.CENTER);
		else
			setHorizontalAlignment(SwingConstants.RIGHT);
		
		return this;
	}
}
