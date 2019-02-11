package jdbc;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import javax.swing.JOptionPane;

// William Wall @ williamwall.ie


public class DatabaseConnection {

	private static Connection dbConnection = null;
	private static Statement statement = null;

	public static Connection getConnection() {

		if (dbConnection != null) {

			return dbConnection;

		} else {

			try {
				// gathers connection properties from file in root directory
				InputStream inputStream = DatabaseConnection.class.getClassLoader()
						.getResourceAsStream("db.properties");
				// properties instantiation to read input stream
				Properties properties = new Properties();
				// load input stream
				properties.load(inputStream);
				// assign string variables to the properties
				String dbDriver = properties.getProperty("dbDriver");
				String connectionUrl = properties.getProperty("connectionUrl");
				String userName = properties.getProperty("userName");
				String password = properties.getProperty("password");
				// instantiate the driver
				Class.forName(dbDriver).newInstance();
				// get connection using properties
				dbConnection = DriverManager.getConnection(connectionUrl, userName, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dbConnection;
		}
	}

	public static void ExecuteSQLStatement(String sql_stmt) {
		try {
			// get db connection
			statement = getConnection().createStatement();
			// runs the sql statement e.g. insert, update, delete...
			statement.executeUpdate(sql_stmt);
		} catch (SQLException ex) {
			// dialog with error message
			JOptionPane.showMessageDialog(null, "The following error has occured: " + ex.getMessage());
		}
	}
}
