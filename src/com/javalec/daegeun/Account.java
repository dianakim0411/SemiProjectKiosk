package com.javalec.daegeun;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.javalec.base.Main;

public class Account extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JRadioButton rdbSelect;
	private JRadioButton rdbUpdate;
	private JRadioButton rdbDelete;
	private JComboBox cbSelection;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField tfSelection;
	private JButton btnSearch;
	private JScrollPane scrollPane;
	private JTable innerTable;
	private JLabel lblLogout;
	private JButton btnCust;
	private JButton btnSales;
	private JButton btnProduct;
	private JButton btnOk;

	// -- Table
	private final DefaultTableModel outerTable = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Account dialog = new Account();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Account() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				tableInit();
				searchAction();
//				screenPartition();
			}
		});
		setTitle("관리자모드");
		setBounds(350, 100, 700, 700);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(119, 108, 106));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getRdbSelect());
		contentPanel.add(getRdbUpdate());
		contentPanel.add(getRdbDelete());
		contentPanel.add(getCbSelection());
		contentPanel.add(getTfSelection());
		contentPanel.add(getBtnSearch());
		contentPanel.add(getScrollPane());
		contentPanel.add(getLblLogout());
		contentPanel.add(getBtnCust());
		contentPanel.add(getBtnSales());
		contentPanel.add(getBtnProduct());
		contentPanel.add(getBtnOk());
	}

	private JRadioButton getRdbSelect() {
		if (rdbSelect == null) {
			rdbSelect = new JRadioButton("회원 조회");
			rdbSelect.setFont(new Font("굴림", Font.BOLD, 20));
			rdbSelect.setForeground(new Color(255, 255, 255));
			rdbSelect.setBackground(new Color(119, 108, 106));
			rdbSelect.setSelected(true);
			buttonGroup.add(rdbSelect);
			rdbSelect.setBounds(100, 88, 140, 23);
		}
		return rdbSelect;
	}

	private JRadioButton getRdbUpdate() {
		if (rdbUpdate == null) {
			rdbUpdate = new JRadioButton("회원 수정");
			rdbUpdate.setFont(new Font("굴림", Font.BOLD, 20));
			rdbUpdate.setForeground(new Color(255, 255, 255));
			rdbUpdate.setBackground(new Color(119, 108, 106));
			buttonGroup.add(rdbUpdate);
			rdbUpdate.setBounds(278, 88, 149, 23);
		}
		return rdbUpdate;
	}

	private JRadioButton getRdbDelete() {
		if (rdbDelete == null) {
			rdbDelete = new JRadioButton("회원 삭제");
			rdbDelete.setFont(new Font("굴림", Font.BOLD, 20));
			rdbDelete.setForeground(new Color(255, 255, 255));
			rdbDelete.setBackground(new Color(119, 108, 106));
			buttonGroup.add(rdbDelete);
			rdbDelete.setBounds(441, 88, 149, 23);
		}
		return rdbDelete;
	}

	private JComboBox getCbSelection() {
		if (cbSelection == null) {
			cbSelection = new JComboBox();
			cbSelection.setModel(new DefaultComboBoxModel(new String[] { "ID", "이름", "주소", "상태" }));
			cbSelection.setBounds(34, 130, 115, 41);
		}
		return cbSelection;
	}

	private JTextField getTfSelection() {
		if (tfSelection == null) {
			tfSelection = new JTextField();
			tfSelection.setBounds(159, 130, 337, 41);
			tfSelection.setColumns(10);
		}
		return tfSelection;
	}

	private JButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new JButton("검색");
			btnSearch.setBounds(506, 130, 149, 41);
		}
		return btnSearch;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(34, 179, 621, 351);
			scrollPane.setViewportView(getInnerTable());
		}
		return scrollPane;
	}

	private JTable getInnerTable() {
		if (innerTable == null) {
			innerTable = new JTable();
			innerTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 1) {
						tableClick();
					}
				}
			});
			innerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// **************************************************
			innerTable.setModel(outerTable);// **********넣어주기
			// **************************************************
		}
		return innerTable;
	}

	private JLabel getLblLogout() {
		if (lblLogout == null) {
			// 원래 이미지의 파일을 icon에 담는다
			ImageIcon icon = new ImageIcon(Account.class.getResource("/com/javalec/images/logout.png"));
			// img에 이미지를 담는다.
			Image img = icon.getImage();
			// 그 이미지를 사이즈 조절해서 담아준다
			Image changeImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			// 변경된 이미지를 다시 ImageIcon에 담아준다
			ImageIcon changeIcon = new ImageIcon(changeImg);
			lblLogout = new JLabel(changeIcon); // 변경된 이미지로 JLabel 생성

			// 바뀌는 이미지 담아주기
			ImageIcon icon2 = new ImageIcon(Account.class.getResource("/com/javalec/images/logout2.png"));
			Image img2 = icon2.getImage();
			Image changeImg2 = img2.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			ImageIcon changeIcon2 = new ImageIcon(changeImg2);

			lblLogout.setToolTipText("로그아웃");
			lblLogout.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					homeScreen();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					lblLogout.setIcon(changeIcon2);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					lblLogout.setIcon(changeIcon);
				}
			});
			lblLogout.setBounds(617, 10, 50, 50);
		}
		return lblLogout;
	}

	// ---- Function ----
	private void homeScreen() {
		// 첫 화면으로 돌아가기
		Main window = new Main();
		window.main(null);
		this.setVisible(false);
	}

	private JButton getBtnCust() {
		if (btnCust == null) {
			btnCust = new JButton("회원");
			btnCust.setBounds(127, 23, 85, 39);
		}
		return btnCust;
	}

	private JButton getBtnSales() {
		if (btnSales == null) {
			btnSales = new JButton("매출");
			btnSales.setBounds(293, 23, 85, 39);
		}
		return btnSales;
	}

	private JButton getBtnProduct() {
		if (btnProduct == null) {
			btnProduct = new JButton("상품");
			btnProduct.setBounds(456, 23, 85, 39);
		}
		return btnProduct;
	}

	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("OK");
			btnOk.setBounds(293, 559, 85, 39);
		}
		return btnOk;
	}

	// Table 초기화 하기
	private void tableInit() {
		// Table Column명 정하기
		outerTable.addColumn("ID");
		outerTable.addColumn("이름");
		outerTable.addColumn("전화번호");
		outerTable.addColumn("주소");
		outerTable.addColumn("상태");
		outerTable.setColumnCount(5); // 갯수를 안 주면 옆으로 계속 늘어난다

		// Table Column 크기 정하기
		int colNo = 0;
		TableColumn col = innerTable.getColumnModel().getColumn(colNo);
		int width = 70;
		col.setPreferredWidth(width);

		colNo = 1;
		col = innerTable.getColumnModel().getColumn(colNo);
		width = 70;
		col.setPreferredWidth(width);

		colNo = 2;
		col = innerTable.getColumnModel().getColumn(colNo);
		width = 100;
		col.setPreferredWidth(width);

		colNo = 3;
		col = innerTable.getColumnModel().getColumn(colNo);
		width = 200;
		col.setPreferredWidth(width);

		colNo = 4;
		col = innerTable.getColumnModel().getColumn(colNo);
		width = 70;
		col.setPreferredWidth(width);

		innerTable.setAutoResizeMode(innerTable.AUTO_RESIZE_ALL_COLUMNS);

		// Table 내용 지우기
		int i = outerTable.getRowCount();
		for (int j = 0; j < i; j++) {
			outerTable.removeRow(0);
		}
	}

	// 검색
	private void searchAction() {
		AccountDao AccountDao = new AccountDao();
		ArrayList<AccountDto> dtoList = AccountDao.selectList();

		int listCount = dtoList.size();

		for (int i = 0; i < listCount; i++) {
			String temp = dtoList.get(i).getCustid();
			String[] qTxt = { temp, dtoList.get(i).getCustname(), dtoList.get(i).getPhone(),
					dtoList.get(i).getAddress(), dtoList.get(i).getDeactivate() };
			outerTable.addRow(qTxt);
		}

	}

	private void conditionQuery() {
		int i = cbSelection.getSelectedIndex();
		String conditionQueryName = "";

		switch (i) {
		case 0:
			conditionQueryName = "ID";
			break;
		case 1:
			conditionQueryName = "이름";
			break;
		case 2:
			conditionQueryName = "주소";
			break;
		case 3:
			conditionQueryName = "상태";
			break;
		default:
			break;
		}

		tableInit(); // 검색한 기록을 지운다
//		clearColumn();
		conditionQueryAction(conditionQueryName); // argument
	}

	private void conditionQueryAction(String conditionQueryName) { // parameter - 타입을 정해준다
		String query1 = "select custid, custname, phone, address, deactivate from customer where " + conditionQueryName;
		String query2 = " like '%" + tfSelection.getText() + "%'";
//		System.out.println(query1 + query2);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
			Statement stmt_mysql = conn_mysql.createStatement(); // 갖고오는것
			
			ResultSet rs = stmt_mysql.executeQuery(query1 + query2);
			while(rs.next()) {
				String[] qTxt = {rs.getString(1)),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5)};
				outerTable.addRow(qTxt);
				
			}
			
			// ConCurentUser : DB에 동시접속자 수 최대 5명 가능.
			conn_mysql.close(); // 반드시 적어줄것
			
		}catch(Exception e) {
			// 에러코드가 있으면 보여주게한다
			e.printStackTrace();
		}
		
	}
	// Table에서 Row를 click했을 경우
	private void tableClick() {
		int i = innerTable.getSelectedRow();
		String tkSequence = (String) innerTable.getValueAt(i, 0);
		int wkSequence = Integer.parseInt(tkSequence);

//			Dao dao = new Dao(wkSequence);
//			Dto dto = dao.tableClick();
//			
//			tfSeqNo.setText(Integer.toString(dto.getSeqno()));
//			tfName.setText(dto.getName());
//			tfTelno.setText(dto.getTelno());
//			tfAddress.setText(dto.getAddress());
//			tfEmail.setText(dto.getEmail());
//			tfRelation.setText(dto.getRelation());
	}

} // End
