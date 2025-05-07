package db.pscore;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ScoreUpdateDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextField[] tf = new JTextField[6];
	private JButton btn1, btn2;
	private ScoreFrame frame;
	private int row;
	private ScoreDAO dao;

	public ScoreUpdateDialog(JFrame frame, String[] items, int row) {
		super(frame, true);

		this.frame = (ScoreFrame) frame;
		this.row = row;
		this.dao = this.frame.getScoreDAO();

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setLayout(null);
		String title[] = { "학번 :", "이름 :", "생년월일 :", "국어 :", "영어 :", "수학 :" };
		for (int i = 0; i < title.length; i++) {
			JLabel lbl = new JLabel(title[i]);
			lbl.setBounds(20, (i + 1) * 30, 80, 22);
			add(lbl);

			tf[i] = new JTextField();
			tf[i].addKeyListener(new KeyHandler());
			tf[i].setBounds(100, (i + 1) * 30, 110, 22);
			add(tf[i]);

			tf[i].setText(items[i]);
		}
		tf[0].setEnabled(false);

		btn1 = new JButton("수정");
		btn1.addKeyListener(new KeyHandler());
		btn1.addActionListener(this);
		btn1.setBounds(30, 220, 80, 22);
		add(btn1);

		btn2 = new JButton("종료");
		btn2.addActionListener(this);
		btn2.setBounds(115, 220, 80, 22);
		add(btn2);

		setTitle("자료 수정");
		setSize(250, 300);
		setLocationRelativeTo(this.frame);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn1) {
			update();
			dispose();
		} else if (e.getSource() == btn2) {
			dispose();
		}
	}

	private void update() {
		String s;

		try {
			for (int i = 0; i < tf.length; i++) {
				s = tf[i].getText().trim();
				if (s.length() == 0) {
					tf[i].requestFocus();
					return;
				}
			}

			// 테이블내용 수정
			ScoreDTO dto = new ScoreDTO();
			dto.setHak(tf[0].getText().trim());
			dto.setName(tf[1].getText().trim());
			dto.setBirth(tf[2].getText().trim());
			dto.setKor(Integer.parseInt(tf[3].getText().trim()));
			dto.setEng(Integer.parseInt(tf[4].getText().trim()));
			dto.setMat(Integer.parseInt(tf[5].getText().trim()));

			int result = dao.updateScore(dto);

			if (result == 1) {
				dto.setTot(dto.getKor() + dto.getEng() + dto.getMat());
				dto.setAve(dto.getTot() / 3);

				frame.tableUpdateRow(dto, row);

				// frame.listScoreAll();
			} else {
				JOptionPane.showMessageDialog(this, "등록된 자료가 아닙니다.");
				return;
			}

		} catch (SQLException e) {
			String msg = "";
			if (e.getErrorCode() == 1400) {
				msg = "에러 - 필수 사항을 입력하지 않았습니다.";
			} else if (e.getErrorCode() == 1861) {
				msg = "에러 - 날짜 입력 형식 오류 입니다.";
			} else {
				msg = "에러 - 데이터 수정이 실패 했습니다.";
			}

			JOptionPane.showMessageDialog(this, msg);
		}
	}

	class KeyHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			Component comp = (Component) e.getSource();
			int code = e.getKeyCode();

			if (code == KeyEvent.VK_ENTER) {
				if (comp instanceof JTextField) {
					for (int i = 0; i < tf.length; i++) {
						if (i == 5 && tf[5] == comp) {
							btn1.requestFocus();
							return;
						} else if (comp == tf[i]) {
							tf[i + 1].requestFocus();
							return;
						}
					}
				} else if (comp == btn1) {
					update();
					dispose();
				}
			}
		}
	}
}
