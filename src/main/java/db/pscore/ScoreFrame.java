package db.pscore;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumn;

public class ScoreFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton btn1, btn2, btn3, btn4, btn5;

	private JComboBox<String> cb;
	private JTextField tf;
	private JButton btnSearch;

	private JTable table;

	private ScoreDAO dao = new ScoreDAOImpl();

	public ScoreFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(5, 1));

		btn1 = new JButton("추가");
		btn1.setBackground(Color.WHITE);
		btn1.setOpaque(true); // 배경색을 주기위해(불투명모드)
		btn1.addActionListener(this);
		p.add(btn1);

		btn2 = new JButton("수정");
		btn2.setBackground(Color.WHITE);
		btn2.setOpaque(true);
		btn2.addActionListener(this);
		p.add(btn2);

		btn3 = new JButton("삭제");
		btn3.setBackground(Color.WHITE);
		btn3.setOpaque(true);
		btn3.addActionListener(this);
		p.add(btn3);

		btn4 = new JButton("새로고침");
		btn4.setBackground(Color.WHITE);
		btn4.setOpaque(true);
		btn4.addActionListener(this);
		p.add(btn4);

		btn5 = new JButton("종료");
		btn5.setBackground(Color.WHITE);
		btn5.setOpaque(true);
		btn5.addActionListener(this);
		p.add(btn5);

		add(p, BorderLayout.EAST);

		p = new JPanel();
		String[] data = { "학번검색", "이름검색" };
		cb = new JComboBox<String>(data);
		cb.setBackground(Color.WHITE);
		cb.setOpaque(true);
		p.add(cb);

		tf = new JTextField(15);
		/*
		 tf.setBorder(BorderFactory.createCompoundBorder( tf.getBorder(), 
		 	BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		 */
		LineBorder border = new LineBorder(new Color(100, 100, 100), 1);
		tf.setBorder(BorderFactory.createCompoundBorder(border, 
				BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		p.add(tf);

		btnSearch = new JButton(" 검색 ");
		btnSearch.setBackground(Color.WHITE);
		btnSearch.setOpaque(true);
		btnSearch.addActionListener(this);
		p.add(btnSearch);

		add(p, BorderLayout.SOUTH);

		// 테이블 추가
		addTable();

		listScoreAll();

		setTitle("성적처리");
		setSize(670, 500);
		setResizable(false);
		setVisible(true);
	}

	private void addTable() {
		String[] title = { "학번", "이름", "생년월일", "국어", "영어", "수학", "총점", "평균" };

		MyTableModel model = new MyTableModel(title);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		add(sp, BorderLayout.CENTER);

		// 자동 크기 변경 OFF
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// 컬럼폭 변경
		for (int i = 0; i < title.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			if (i == 1) {
				col.setPreferredWidth(100);
			} else if (i == 2) {
				col.setPreferredWidth(80);
			} else {
				col.setPreferredWidth(60);
			}
		}

		// 컬럼 정렬 방식 변경
		MyTableCellRenderer render = new MyTableCellRenderer();
		table.setDefaultRenderer(table.getColumnClass(0), render);
	}

	public ScoreDAO getScoreDAO() {
		return dao;
	}

	public void tableInsertRow(String[] items) {
		// 테이블에 자료 추가
		((MyTableModel) table.getModel()).addRow(items);
	}

	public void tableInsertRow(ScoreDTO dto) {
		// 테이블에 자료 추가
		String[] items = new String[8];

		items[0] = dto.getHak();
		items[1] = dto.getName();
		items[2] = dto.getBirth();
		items[3] = Integer.toString(dto.getKor());
		items[4] = Integer.toString(dto.getEng());
		items[5] = Integer.toString(dto.getMat());
		items[6] = Integer.toString(dto.getTot());
		items[7] = Integer.toString(dto.getAve());

		((MyTableModel) table.getModel()).addRow(items);
	}

	public void tableUpdateRow(String[] items, int row) {
		// 해당 행의 데이터 수정
		for (int i = 0; i < items.length; i++) {
			((MyTableModel) table.getModel()).setValueAt(items[i], row, i);
		}
	}

	public void tableUpdateRow(ScoreDTO dto, int row) {
		// 해당 행의 데이터 수정
		String[] items = new String[8];

		items[0] = dto.getHak();
		items[1] = dto.getName();
		items[2] = dto.getBirth();
		items[3] = Integer.toString(dto.getKor());
		items[4] = Integer.toString(dto.getEng());
		items[5] = Integer.toString(dto.getMat());
		items[6] = Integer.toString(dto.getTot());
		items[7] = Integer.toString(dto.getAve());

		for (int i = 0; i < items.length; i++) {
			((MyTableModel) table.getModel()).setValueAt(items[i], row, i);
		}
	}

	public void tableRemoveRow(int row) {
		// 테이블의 특정 행 지우기
		((MyTableModel) table.getModel()).removeRow(row);
	}

	public void tableRemoveAll() {
		// 테이블의 모든 행 지우기
		int size = table.getRowCount();
		for (int i = 0; i < size; i++)
			((MyTableModel) table.getModel()).removeRow(0);
	}

	public void tableInsertList(List<ScoreDTO> list) {
		// 테이블의 모든 자료를 지우고 List 데이터를 추가하기
		tableRemoveAll();

		for (ScoreDTO dto : list) {
			tableInsertRow(dto);
		}
	}

	public void listScoreAll() {
		List<ScoreDTO> list = dao.listScore();
		tableInsertList(list);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn1) {
			// 추가
			new ScoreInsertDialog(this);

		} else if (e.getSource() == btn2) {
			// 수정
			int idx = table.getSelectedRow();
			if (idx == -1) {
				JOptionPane.showMessageDialog(this, "수정할 자료를 먼저 선택하세요.");
				return;
			}

			int cnt = ((MyTableModel) table.getModel()).getColumnCount();
			String[] items = new String[cnt];
			for (int i = 0; i < cnt; i++)
				items[i] = table.getValueAt(idx, i).toString();

			new ScoreUpdateDialog(this, items, idx);

		} else if (e.getSource() == btn3) {
			// 삭제
			int idx = table.getSelectedRow();
			if (idx == -1) {
				JOptionPane.showMessageDialog(this, "삭제할 자료를 먼저 선택하세요.");
				return;
			}

			int result;
			result = JOptionPane.showConfirmDialog(this, "선택한 자료를 삭제하시겠습니까 ?", "확인", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				// 테이블 내용 지우기
				String hak = table.getValueAt(idx, 0).toString();
				try {
					dao.deleteScore(hak);
				} catch (Exception e2) {
				}
				tableRemoveRow(idx);
			}

		} else if (e.getSource() == btn4) {
			// 새로 고침
			listScoreAll();

			cb.setSelectedIndex(0);
			tf.setText("");

		} else if (e.getSource() == btn5) {
			// 종료
			int result = JOptionPane.showConfirmDialog(this, "종료하시겠습니까 ?", "확인", 
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		} else if (e.getSource() == btnSearch) {
			// 검색
			String searchValue;

			int idx = cb.getSelectedIndex();
			searchValue = tf.getText().trim();

			if (searchValue.length() == 0)
				return;

			if (idx == 0) {
				// 학번검색
				ScoreDTO dto = dao.findById(searchValue);
				tableRemoveAll();

				if (dto != null) {
					tableInsertRow(dto);
				}
			} else {
				// 이름검색
				List<ScoreDTO> list = dao.listScore(searchValue);
				tableInsertList(list);
			}
		}
	}
}
