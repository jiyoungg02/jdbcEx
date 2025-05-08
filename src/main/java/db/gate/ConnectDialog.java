package db.gate;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import db.util.DBConn;

public class ConnectDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	String caption[] = { "서버 :", "유저명 :", "패스워드 :", "서비스명 :" };

	private JTextField tf[] = new JTextField[4];
	private JComboBox<String> connMode;
	private JButton btn1, btn2;

	private Connection conn = null;
	private String connectUser = "";

	public ConnectDialog(JFrame frame) {
		super(frame, true);
		
		setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JLabel lbl = null;
		for (int i = 0; i < 4; i++) {
			lbl = new JLabel();
			lbl.setText(caption[i]);
			lbl.setBounds(20, i * 35 + 15, 60, 25);
			add(lbl);

			tf[i] = new JTextField();
			tf[i].setBorder(BorderFactory.createCompoundBorder(
					tf[i].getBorder(),
			        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
			this.tf[i].setBounds(90, i * 35 + 15, 108, 25);
			// 패스워드는 JPassword 컴포넌트 사용
			tf[i].addKeyListener(new KeyHandler());
			add(this.tf[i]);
		}

		tf[0].setText("127.0.0.1");
		tf[1].setText("sky");
		tf[2].setText("java$!");
		tf[3].setText("XE");

		connMode = new JComboBox<String>();
		connMode.setBackground(Color.WHITE);
		connMode.setOpaque(true);
		connMode.addItem("Normal");
		connMode.addItem("SYSDBA");
		connMode.addItem("SYSOPER");
		lbl = new JLabel("연결모드 :");
		lbl.setBounds(20, 155, 60, 25);
		add(lbl);
		connMode.setBounds(90, 155, 108, 25);
		add(connMode);

		btn1 = new JButton("서버접속");
		btn1.setActionCommand("conn");
		btn1.addActionListener(this);
		btn1.addKeyListener(new KeyHandler());
		btn1.setBackground(Color.WHITE);
		btn1.setOpaque(true);
		btn1.setBounds(20, 195, 88, 25);
		add(btn1);

		btn2 = new JButton("종료");
		btn2.setActionCommand("exit");
		btn2.addActionListener(this);
		btn2.setBackground(Color.WHITE);
		btn2.setOpaque(true);
		btn2.setBounds(110, 195, 88, 25);
		add(btn2);
		
		setTitle("서버 접속");
		setResizable(false);
		setSize(230, 280);
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("conn")) {
			connect();
		} else if (evt.getActionCommand().equals("exit")) {
			dispose();
		}
	}
	
	// 데이터베이스 연결
	private void connect() {
		if (conn != null) {
			return;
		}

		String url = tf[0].getText();
		String user = tf[1].getText();
		String pwd = tf[2].getText();
		String ocl = tf[3].getText();
		String mode = (String) connMode.getSelectedItem();

		// url = "jdbc:oracle:thin:@" + url + ":1521:" + ocl;
		url = "jdbc:oracle:thin:@//" + url + ":1521/" + ocl;

		conn = DBConn.getConnection(url, user, pwd, mode);

		if (conn == null) {
			JOptionPane.showMessageDialog(this, "데이터베이스가 연결에 실패 했습니다.", "정보",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		this.connectUser = tf[1].getText();

		JOptionPane.showMessageDialog(this, "데이터베이스에 연결되었습니다.", "정보",
				JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public String getConnectUser() {
		return connectUser;
	}

	// DB 연결 해제
	public void disConnect() {
		if (conn != null) {
			DBConn.close();
		}

		conn = null;
		connectUser = "";
		JOptionPane.showMessageDialog(this, "데이터베이스가 연결이 종료 되었습니다.", "정보", 	JOptionPane.INFORMATION_MESSAGE);
	}

	// 키보드 이벤트 처리
	class KeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent evt) {
			Component ob = (Component) evt.getSource();

			int keyCode = evt.getKeyCode();
			int i;

			if (ob instanceof JTextField) {
				if (keyCode == KeyEvent.VK_ENTER) {

					for (i = 0; i < 4; i++) {
						if (ob == tf[3] && i == 3) {
							connMode.requestFocus();
						} else if (ob == tf[i]) {
							tf[i + 1].requestFocus();
						}
					}
				}
			}

			else if (ob instanceof JButton) {
				if (keyCode == KeyEvent.VK_ENTER) {
					if ((JButton) evt.getSource() == btn1) {
						connect();
					} else if ((JButton) evt.getSource() == btn2) {
						dispose();
					}
				}
			}
		} // keyPressed end
	}
}
