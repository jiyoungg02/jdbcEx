package db.gate;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableColumn;

public class ResultPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	private ResultQueryPanel resultQueryPanel;
	private ResultMessagePanel resultMsgPanel;

	public ResultPane() {
		resultQueryPanel = new ResultQueryPanel();
		resultMsgPanel = new ResultMessagePanel();
		addTab("▶ 질의 결과", resultQueryPanel);
		addTab("메시지", resultMsgPanel);
	}

	public void clearResult() {
		resultQueryPanel.clear();
		resultMsgPanel.clear();
		this.repaint();
	}

	public void resultQuery(String columnNames[], List<String[]> list) {
		resultQueryPanel.resultView(columnNames, list);
	}

	public void resultQuery(String columnNames[], String[] items) {
		resultQueryPanel.resultView(columnNames, items);
	}

	public void message(String msg) {
		resultMsgPanel.setMessage(msg);
	}

	// SELECT 문의 결과 출력 Panel
	class ResultQueryPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private JTable table;
		private JScrollPane sp;

		public ResultQueryPanel() {
			setLayout(new BorderLayout());
		}

		public void clear() {
			if (sp != null) {
				remove(sp);
				sp = null;
				table = null;
			}
		}

		public void resultView(String columnNames[], List<String[]> list) {
			addTable(columnNames);

			for (String[] items : list) {
				tableInsertRow(items);
			}
		}

		public void resultView(String columnNames[], String[] items) {
			addTable(columnNames);

			tableInsertRow(items);
		}

		private void addTable(String columnNames[]) {
			clear();

			MyTableModel model = new MyTableModel(columnNames);
			table = new JTable(model);

			// JTable에 스크롤바를 추가하기 위해 JScrollPane에 추가
			sp = new JScrollPane(table);

			// JTable을 추가
			add(sp);

			// JTable 폭을 자동으로 변경하지 못하도록 설정(기본 자동변경)
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			// 컬럼폭 지정
			for (int i = 0; i < columnNames.length; i++) {
				TableColumn tc = table.getColumnModel().getColumn(i);
				tc.setPreferredWidth(100);
			}
		}

		private void tableInsertRow(String[] items) {
			// 테이블에 자료 추가
			((MyTableModel) table.getModel()).addRow(items);
		}
	} // end_class ResultQueryPanel

	// 메시지 출력 Panel
	class ResultMessagePanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private JTextPane tp;

		public ResultMessagePanel() {
			setLayout(new BorderLayout());

			tp = new JTextPane();
			tp.setEditable(false);
			JScrollPane sp = new JScrollPane(tp);
			add(sp);
		}

		public void setMessage(String msg) {
			tp.setText(msg);
		}

		public void clear() {
			tp.setText("");
		}

	} // end_class ResultMsgPanel
} // end_class ResultPane
