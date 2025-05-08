package db.gate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

public class Worksheet extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	private MyGateFrame frame;
	private ResultPane resultPane;
	private WorksheetPanel worksheetPanel;

	public Worksheet(JFrame frame, ResultPane resultPane) {
		this.frame = (MyGateFrame) frame;
		this.resultPane = resultPane;

		worksheetPanel = new WorksheetPanel();
		addTab("워크시트", worksheetPanel);
	}

	// 쿼리 입력 내용 지우기
	public void clearQuery() {
		worksheetPanel.clear();
	}

	// 쿼리 입력 화면
	class WorksheetPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		private JButton btnResult;
		private JButton btnClear;
		private JTextPane textQuery;

		public WorksheetPanel() {
			setLayout(new BorderLayout());

			JToolBar tb = new JToolBar();
			tb.setFloatable(false); // 툴바 고정
			tb.addSeparator();

			btnResult = new JButton(" ▶ ");
			btnResult.setToolTipText("실행");
			btnResult.addActionListener(this);
			btnResult.setBackground(Color.WHITE);
			btnResult.setOpaque(true);
			tb.add(btnResult);

			btnClear = new JButton("  C  ");
			btnClear.setToolTipText("Clear");
			btnClear.addActionListener(this);
			btnClear.setBackground(Color.WHITE);
			btnClear.setOpaque(true);
			tb.add(btnClear);

			add(tb, BorderLayout.NORTH);

			textQuery = new JTextPane();
			textQuery.setFont(new Font("Consolas", Font.PLAIN, 20));
			textQuery.addKeyListener(new KeyHandler());
			add(textQuery);
		}

		public void clear() {
			textQuery.setText("");
			textQuery.requestFocus();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnResult) {
				execute();
			} else if (e.getSource() == btnClear) {
				int result;
				result = JOptionPane.showConfirmDialog(this, "입력한 쿼리를 삭제 하시겠습니까 ?", "확인", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					clear();
					resultPane.clearResult();
				}

			}
		}

		// 쿼리 실행
		public void execute() {
			String query = getQuery();

			MyGateDAO dao = frame.getMyGateDAO();
			if (dao == null) {
				JOptionPane.showMessageDialog(this, "데이터베이스 접속을 먼저 실행해야 합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			query = query.trim();
			if (query.length() == 0)
				return;

			if (query.lastIndexOf(";") == query.length()-1) {
				query = query.substring(0, query.length()-1);
			}

			if (query.toLowerCase().startsWith("select")) {
				executeQuery(query);
			} else if (query.toLowerCase().startsWith("desc")) {
				executeMeta(query);
			} else {
				executeUpdate(query);
			}
		}

		// 쿼리창에 입력한 쿼리
		private String getQuery() {
			String query = textQuery.getText().trim();
			if (query.length() == 0)
				return "";

			if (textQuery.getSelectedText() != null) {
				String s = textQuery.getSelectedText().trim();
				if (s.length() != 0) {
					query = s;
				}
			}

			return query;
		}

		// SELECT 문 실행
		private void executeQuery(String sql) {
			String message;

			MyGateDAO dao = frame.getMyGateDAO();
			if (dao == null) {
				return;
			}

			resultPane.requestFocus(); // 변경된 table 정보를 출력하기 위해

			try {
				List<String[]> list = dao.getResultList(sql);
				String columnNames[] = dao.getColumnNames();

				// 결과 출력
				resultPane.setSelectedIndex(0);
				resultPane.resultQuery(columnNames, list);
			} catch (SQLException e) {
				message = e.getMessage();

				resultPane.setSelectedIndex(1);
				resultPane.message(message);
			} catch (Exception e) {
				message = "오류가 발생 했습니다.";

				resultPane.setSelectedIndex(1);
				resultPane.message(message);
			}

			textQuery.requestFocus();
		}

		// INSERT, UPDATE, DELETE, CREATE 등 실행
		private void executeUpdate(String sql) {
			String message;

			MyGateDAO dao = frame.getMyGateDAO();
			if (dao == null) {
				return;
			}

			resultPane.requestFocus();

			try {
				message = dao.execute(sql);

				// 실행 결과 메시지 출력
				resultPane.setSelectedIndex(1);
				resultPane.message(message);
			} catch (SQLException e) {
				message = e.getMessage();

				resultPane.setSelectedIndex(1);
				resultPane.message(message);
			} catch (Exception e) {
				message = "오류가 발생 했습니다.";

				resultPane.setSelectedIndex(1);
				resultPane.message(message);
			}

			textQuery.requestFocus();
		}

		// DESC 명령 실행
		private void executeMeta(String sql) {
			String message;

			MyGateDAO dao = frame.getMyGateDAO();
			if (dao == null) {
				return;
			}

			String str[] = sql.split("\\s");
			if (str.length != 2) {
				JOptionPane.showMessageDialog(this, "형식 오류 !!!", "정보", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			try {
				// cols 또는 user_tab_columns 자료사전을 이용하여 정보를 구할 수 있다.
				sql = "SELECT * FROM " + str[1];
				List<String[]> list = dao.getDescList(sql);

				String columnNames[] = { "컬럼명", "널", "형식" };

				// 결과 출력
				resultPane.requestFocus();
				resultPane.setSelectedIndex(0);
				resultPane.resultQuery(columnNames, list);

			} catch (SQLException e) {
				message = e.getMessage();
				JOptionPane.showMessageDialog(this, message, "정보", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				message = "오류가 발생 했습니다.";
				JOptionPane.showMessageDialog(this, message, "정보", JOptionPane.INFORMATION_MESSAGE);
			}

			textQuery.requestFocus();
		}

		class KeyHandler extends KeyAdapter {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				if (e.isControlDown() && keyCode == KeyEvent.VK_ENTER) {
					if (e.getSource() == textQuery) {
						execute();
					}
				}
			}
		} // end_class KeyHandler
	} // end_class WorksheetPanel
} // end_class Worksheet