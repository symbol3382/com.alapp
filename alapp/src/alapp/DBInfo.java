package alapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBInfo {
	private static Connection connection = null;
	final static private String IP = "localhost";
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"+IP+":3306/alapp", "root", "");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	public static void closeConnection() {
		try {
			connection.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}