package model;

import java.sql.*;
import javax.swing.table.AbstractTableModel;

import jdbc.DatabaseConnection;

// William Wall @ williamwall.ie


public class TableModel extends AbstractTableModel {

	private Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	public TableModel(String query) throws SQLException {

		// get connection for DatabaseConnection class @ jdbc package
		connection = DatabaseConnection.getConnection();

		// identify resultset is scrollable
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		// execute the query
		resultSet = statement.executeQuery(query);

		// method to get metadata from the ResultSet object
		metaData = resultSet.getMetaData();

		// moves the cursor to the last row in the ResultSet object
		resultSet.last();

		numberOfRows = resultSet.getRow();

		// notifies all listeners that the table's structure has changed.
		fireTableStructureChanged();

	}

	// generic table implementation methods
	@Override
	public Class getColumnClass(int column) throws IllegalStateException {
		try {

			String className = metaData.getColumnClassName(column + 1);
			return Class.forName(className);

		} catch (ClassNotFoundException | SQLException ex) {

			System.out.println(ex.getMessage());

		}

		return Object.class;
	}

	@Override
	public int getColumnCount() throws IllegalStateException {
		try {

			return metaData.getColumnCount();

		} catch (SQLException ex) {

			System.out.println(ex.getMessage());
		}

		return 0;
	}

	@Override
	public String getColumnName(int column) throws IllegalStateException {
		try {

			return metaData.getColumnName(column + 1);

		} catch (SQLException ex) {

			System.out.println(ex.getMessage());
		}

		return "";
	}

	@Override
	public int getRowCount() throws IllegalStateException {

		return numberOfRows;
	}

	@Override
	public Object getValueAt(int row, int column) throws IllegalStateException {

		try {

			resultSet.absolute(row + 1);

			return resultSet.getObject(column + 1);

		} catch (SQLException ex) {

			System.out.println(ex.getMessage());

		}

		return "";
	}

	public void disconnectFromDatabase() {

		try {

			resultSet.close();
			statement.close();
			connection.close();

		} catch (SQLException ex) {

			System.out.println(ex.getMessage());

		}
	}
}
