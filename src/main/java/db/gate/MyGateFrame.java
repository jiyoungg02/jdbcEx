package db.gate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class MyGateFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton btn1, btn2, btn3;
	private JLabel lblUser;

	private ConnectDialog connectDialog;
	private MyGateDAO dao;

	private Worksheet workPane;
	private ResultPane queryPane;

	public MyGateFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar tb = new JToolBar();

		btn1 = new JButton("접속");
		btn1.addActionListener(this);
		btn1.setBackground(Color.WHITE);
		btn1.setOpaque(true);
		tb.add(btn1);

		btn2 = new JButton("접속해제");
		btn2.addActionListener(this);
		btn2.setBackground(Color.WHITE);
		btn2.setOpaque(true);
		tb.add(btn2);

		tb.addSeparator();
		btn3 = new JButton("종료");
		btn3.addActionListener(this);
		btn3.setBackground(Color.WHITE);
		btn3.setOpaque(true);
		tb.add(btn3);

		tb.addSeparator();
		lblUser = new JLabel("", JLabel.CENTER);
		tb.add(lblUser);

		add(tb, BorderLayout.NORTH);

		queryPane = new ResultPane();
		workPane = new Worksheet(this, queryPane);

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setTopComponent(new JScrollPane(workPane));
		split.setBottomComponent(new JScrollPane(queryPane));
		split.setDividerLocation(500);

		BasicSplitPaneDivider divider = ((BasicSplitPaneUI) split.getUI()).getDivider();
		divider.setDividerSize(3);

		add(split);

		init();

		setTitle("My Gate");
		setSize(850, 800);
		// setExtendedState(JFrame.MAXIMIZED_BOTH); // 창 최대화
		setVisible(true);
	}

	public MyGateDAO getMyGateDAO() {
		return dao;
	}

	private void init() {
		connectDialog = null;
		dao = null;
		btn1.setEnabled(true);
		btn2.setEnabled(false);
		lblUser.setText("");

		workPane.clearQuery();
		queryPane.clearResult();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn1) {
			connectDialog = new ConnectDialog(this);
			Connection conn = connectDialog.getConnection();

			if (conn == null) {
				init();
				return;
			}

			lblUser.setText("접속 유저 : " + connectDialog.getConnectUser());

			dao = new MyGateDAO(conn);

			btn1.setEnabled(false);
			btn2.setEnabled(true);

		} else if (e.getSource() == btn2) {
			if (connectDialog != null) {
				connectDialog.disConnect();
			}

			init();

		} else if (e.getSource() == btn3) {
			if (connectDialog != null) {
				connectDialog.disConnect();
			}

			System.exit(0);
		}
	}
}
