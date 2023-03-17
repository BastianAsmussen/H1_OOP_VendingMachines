package tech.asmussen.sql;

import java.sql.*;

public final class MySQL {
	
	public static Connection connect(String host, int port, String database, String user, String password)
			throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		final String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
		
		return DriverManager.getConnection(url, user, password);
	}
	
	public static ResultSet query(Connection connection, String query) {
		
		try {
			
			PreparedStatement statement = connection.prepareStatement(query);
			
			if (!statement.execute()) {
				
				return null;
			}
			
			return statement.getResultSet();
			
		} catch (SQLException e) {
			
			return null;
		}
	}
}
