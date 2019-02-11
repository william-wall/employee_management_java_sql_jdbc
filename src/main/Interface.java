package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;

import jdbc.DatabaseConnection;
import model.TableModel;

// William Wall @ williamwall.ie


public class Interface extends javax.swing.JDialog {

	private javax.swing.JTable jTable1;
	private javax.swing.JTextField txtSSN;
	private javax.swing.JTextField txtDOB;
	private javax.swing.JTextField txtName;
	private javax.swing.JTextField txtAddress;
	private javax.swing.JTextField txtSalary;
	private javax.swing.JComboBox<String> dropDownGender;
	// next and previous operations counter
	int navCounter = 1;
	boolean addRecord = false;
	String sql;

	// clear text fields
	private void clearInputBoxes() {

		txtSSN.setText("");
		txtDOB.setText("");
		txtName.setText("");
		txtAddress.setText("");
		txtSalary.setText("");
		dropDownGender.setSelectedItem("");

	}

	// add new record
	private void addEmployee() {

		String sql_stmt = "INSERT INTO employee (ssn, dob, name, address, salary, gender)";

		sql_stmt += " VALUES ('" + txtSSN.getText() + "','" + txtDOB.getText() + "','" + txtName.getText() + "','"
				+ txtAddress.getText() + "','" + txtSalary.getText() + "','"
				+ dropDownGender.getSelectedItem().toString() + "')";

		// validate text fields
		if (txtSSN.getText().isEmpty() || txtDOB.getText().isEmpty() || txtName.getText().isEmpty()
				|| txtAddress.getText().isEmpty() || txtSalary.getText().isEmpty()) {
			// error message dialog
			JOptionPane.showMessageDialog(null, "Empty fields, please fill all fields!");

		} else {
			// use class method from jdbc to execute
			DatabaseConnection.ExecuteSQLStatement(sql_stmt);

		}

	}

	// update a record
	private void updateEmployee() {

		String sql_stmt = "UPDATE employee SET ssn = '	" + txtSSN.getText() + "'";

		sql_stmt += ",dob = '" + txtDOB.getText() + "'";
		sql_stmt += ",name = '" + txtName.getText() + "'";
		sql_stmt += ",address = '" + txtAddress.getText() + "'";
		sql_stmt += ",salary = '" + txtSalary.getText() + "'";
		sql_stmt += ",gender = '" + dropDownGender.getSelectedItem().toString() + "'";
		sql_stmt += " WHERE ssn = '" + txtSSN.getText() + "'";

		// use class method from jdbc to execute
		DatabaseConnection.ExecuteSQLStatement(sql_stmt);
	}

	// delete a record
	private void deleteEmployee() {

		String sql_stmt = "DELETE FROM employee WHERE ssn = '" + txtSSN.getText() + "'";

		// use class method from jdbc to execute
		DatabaseConnection.ExecuteSQLStatement(sql_stmt);

	}

	// move to next record
	private void nextRecord() {

		try {

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");

			navCounter++;

			try {

				Statement stmt = con.createStatement();

				sql = "select * from employee where ssn=" + navCounter;

				ResultSet rs = stmt.executeQuery(sql);

				// move to next record
				if (rs.next()) {
					txtSSN.setText(rs.getString(1));
					txtDOB.setText(rs.getString(2));
					txtName.setText(rs.getString(3));
					txtAddress.setText(rs.getString(4));
					txtSalary.setText(rs.getString(5));
					dropDownGender.setSelectedItem(rs.getString(6));
				}
			} catch (Exception e4) {
				System.out.println(e4);
			}
		} catch (Exception e) {
		}
	}

	// move to previous record
	private void previousRecord() {

		if (navCounter <= 1) {

			navCounter = 1;

			JOptionPane.showMessageDialog(null, "There are no records less then 1 SSN");

		} else {

			navCounter--;

		}
		try {

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");

			Statement stmt = con.createStatement();

			sql = "select * from employee where ssn=" + navCounter;

			ResultSet rs = stmt.executeQuery(sql);

			rs = stmt.executeQuery(sql);

			// move to previous record
			if (rs.next()) {
				txtSSN.setText(rs.getString(1));
				txtDOB.setText(rs.getString(2));
				txtName.setText(rs.getString(3));
				txtAddress.setText(rs.getString(4));
				txtSalary.setText(rs.getString(5));
				dropDownGender.setSelectedItem(rs.getString(6));
			}
		} catch (Exception e5) {
			System.out.println(e5);
		}
	}

	// load records
	private void loadRecords() throws SQLException {

		String sql_stmt = "SELECT * FROM employee;";

		// instantiate TableModel class with all employee query
		TableModel tableModel = new TableModel(sql_stmt);

		// set tablemodel instance to JTable
		jTable1.setModel(tableModel);

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");

		Statement st = con.createStatement();

		// auto loading first record into text fields
		ResultSet rs = st.executeQuery("select * from employee where ssn=" + 1);

		String ssn = "", dob = "", name = "", address = "", salary = "", gender = "";

		if (rs.next()) {
			ssn = rs.getString("ssn");
			dob = rs.getString("dob");
			name = rs.getString("name");
			address = rs.getString("address");
			salary = rs.getString("salary");
			gender = rs.getString("gender");

		}

		// setting fields on load results
		txtSSN.setText(ssn);
		txtDOB.setText(dob);
		txtName.setText(name);
		txtAddress.setText(address);
		txtSalary.setText(salary);
		dropDownGender.setSelectedItem(gender);

		// add listener to JTable for click
		jTable1.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {

			try {

				if (jTable1.getSelectedRow() >= 0) {

					// getting values
					Object ssn1 = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
					Object dob1 = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
					Object name1 = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
					Object address1 = jTable1.getValueAt(jTable1.getSelectedRow(), 3);
					Object salary1 = jTable1.getValueAt(jTable1.getSelectedRow(), 4);
					Object gender1 = jTable1.getValueAt(jTable1.getSelectedRow(), 5);

					// setting values
					txtSSN.setText(ssn1.toString());
					txtDOB.setText(dob1.toString());
					txtName.setText(name1.toString());
					txtAddress.setText(address1.toString());
					txtSalary.setText(salary1.toString());
					dropDownGender.setSelectedItem(gender1.toString());

					// getting value of the click and setting it to navCounter to ensure next and
					// previous buttons move to correct record
					int rowNum = (int) ssn1;
					navCounter = rowNum;

				}

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		});

		// rendering and displaying individual cells in a JTable
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();

		// align
		rightRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		jTable1.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);

	}

	public Interface(java.awt.Frame parent, boolean modal) {

		super(parent, modal);

		// initiate the components method
		initComponents();

	}

	private void initComponents() {

		// panel instance
		javax.swing.JPanel jPanel1 = new javax.swing.JPanel();

		// text JLabels instances
		javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
		txtSSN = new javax.swing.JTextField();

		javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
		txtDOB = new javax.swing.JTextField();

		javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
		txtName = new javax.swing.JTextField();

		javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
		txtAddress = new javax.swing.JTextField();

		javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
		txtSalary = new javax.swing.JTextField();

		javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
		dropDownGender = new javax.swing.JComboBox<>();

		// button instances
		javax.swing.JButton btnAddNew = new javax.swing.JButton();
		javax.swing.JButton btnUpdate = new javax.swing.JButton();
		javax.swing.JButton btnDelete = new javax.swing.JButton();
		javax.swing.JButton btnClear = new javax.swing.JButton();
		javax.swing.JButton btnPrevious = new javax.swing.JButton();
		javax.swing.JButton btnClose = new javax.swing.JButton();
		javax.swing.JButton btnNext = new javax.swing.JButton();

		// scrollpane instances
		javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();

		// JTable instance
		jTable1 = new javax.swing.JTable();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowOpened(java.awt.event.WindowEvent evt) {

				formWindowOpened(evt);

			}
		});

		// setting texts and layouts
		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 3, 25)));

		jLabel2.setText("Social Security Number (SSN):");

		jLabel3.setText("Date of Birth (DOB):");

		jLabel4.setText("Full Name:");

		jLabel5.setText("Address:");

		jLabel6.setText("Salary:");

		jLabel7.setText("Gender:");

		dropDownGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female", "Other" }));

		// grouplayout distribution
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);

		jPanel1.setLayout(jPanel1Layout);

		jPanel1Layout.setHorizontalGroup(

				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

						.addGroup(jPanel1Layout.createSequentialGroup()

								.addContainerGap()

								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel7).addComponent(jLabel6).addComponent(jLabel5)
										.addComponent(jLabel4).addComponent(jLabel3).addComponent(jLabel2))

								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(txtDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(dropDownGender, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtSSN, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
														javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addContainerGap()));

		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()

						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2).addComponent(txtSSN, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3).addComponent(txtDOB, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel4).addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel5).addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel6).addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel7).addComponent(dropDownGender,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))

						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		// add listener
		btnAddNew.setText("Add");
		btnAddNew.addActionListener(evt -> {
			addRecord = true;
			btnUpdateActionPerformed(evt);
		});

		// update listener
		btnUpdate.setText("Update");
		btnUpdate.addActionListener(evt -> {
			addRecord = false;
			btnUpdateActionPerformed(evt);
		});

		// delete listener
		btnDelete.setText("Delete");
		btnDelete.addActionListener(this::btnDeleteActionPerformed);

		// clear listener
		btnClear.setText("Clear Fields");
		btnClear.addActionListener(this::btnClearActionPerformed);

		// next listener
		btnNext.setText("Next");
		btnNext.addActionListener(this::btnNextActionPerformed);

		// previous listener
		btnPrevious.setText("Previous");
		btnPrevious.addActionListener(this::btnPreviousActionPerformed);

		// close listener
		btnClose.setText("Close");
		btnClose.addActionListener(this::btnCloseActionPerformed);

		// set model to JTable
		jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {}));

		// auto resize i.e. drag
		jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

		// scrollpane on the jTable
		jScrollPane1.setViewportView(jTable1);

		// grouplayout instance
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

		// set the layout on group instance
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
						.createSequentialGroup().addComponent(btnAddNew)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnUpdate)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnDelete)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnPrevious)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnNext)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
						.addComponent(btnClear)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnClose))
						.addGroup(layout.createSequentialGroup().addGap(5, 5, 5).addComponent(jScrollPane1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(btnAddNew)
						.addComponent(btnUpdate).addComponent(btnDelete).addComponent(btnPrevious).addComponent(btnNext)
						.addComponent(btnClose).addComponent(btnClear))
				.addContainerGap(21, Short.MAX_VALUE)));
		pack();
	}

	// on open load the records
	private void formWindowOpened(java.awt.event.WindowEvent evt) {
		try {

			loadRecords();

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// clear fields
	private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {

		clearInputBoxes();

	}

	// delete selected record and reload
	private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {

		int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?",
				"Confirm Delete Record?", JOptionPane.YES_NO_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION) {
			try {

				deleteEmployee();

				loadRecords();

			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	// move to next record
	private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {

		int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want the next record?",
				"Confirm Delete Record?", JOptionPane.YES_NO_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION) {

			nextRecord();

		}
	}

	// move to previous record
	private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {

		int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want the Previous record?",
				"Confirm Delete Record?", JOptionPane.YES_NO_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION) {

			previousRecord();
		}
	}

	// close window
	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {

		int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the system",
				"Confirm Close Window?", JOptionPane.YES_NO_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION) {

			System.exit(0);
		}
	}

	// update record
	private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {

		int dialogResult = JOptionPane.showConfirmDialog(null,
				addRecord ? "Confirm Add new record?" : "Are you sure you want to update this record?",
				addRecord ? "Confirm Add Record" : "Confirm Update Record?", JOptionPane.YES_NO_OPTION);

		if (dialogResult == JOptionPane.YES_OPTION) {
			try {

				if (addRecord) {

					addEmployee();

					clearInputBoxes();

				} else {

					updateEmployee();
				}
				loadRecords();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public static void main(String args[]) {
		try {

			// layout styles
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		// create and display the dialog
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				Interface dialog = new Interface(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}
}
