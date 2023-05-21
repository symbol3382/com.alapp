package alapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
	private static Connection connection = null;
	final static private String IP = "localhost";
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"+IP+":3306/alapp", "root", "11221122");
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