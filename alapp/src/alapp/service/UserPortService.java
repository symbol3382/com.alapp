package alapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alapp.config.DatabaseConfig;
import alapp.model.UserPort;

public class UserPortService {

	private UserPort userPort;
	private JFrame frame; // For dialog box position
	
	/* Database variables */
	private PreparedStatement preparedStatement;
	private Connection connection;
	private ResultSet resultSet;

	/*
	 * Instance variable
	 */
	String id;
	
	public UserPortService(String id, JFrame frame) {
		userPort = new UserPort();
		userPort.setId(id);
		this.id = id;
		this.frame = frame;
		preparedStatement = null;
		connection = DatabaseConfig.getConnection();
	}
	
	/* 
	 * Getter setter
	 */
	public UserPort getUserPort() {
		return userPort;
	}

	public void setUserPort(UserPort userPort) {
		this.userPort = userPort;
	}

	
	/*
	 * initialize port number
	 * generate random port number 
	 * free current user from user_port
	 * check port is free or not otherwise regenerate
	 * save port to database table `user_port`
	 */
	
	final public void initializePortNumber() {
		userPort.setPort(generatePortNumber());
		freeIPPort(frame);
		if (checkPortIsFree(frame)) {
			savePortToDatabase(frame);
		} else {
			initializePortNumber();
		}
	}
	

	/* 
	 * Create random port number only
	 */
	final private String generatePortNumber() {
		String port = "";
		for (int i = 0; i < 5; i++) {
			port = port + (int) (Math.random() * 10);
		}
		return port;
	}
	/*
	 * To free port on exit or logout
	 */
	final public void freeIPPort(JFrame frame) {
		String query = "DELETE FROM `user_port` WHERE `id`=?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userPort.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Can't free ports", "Erroe", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * Step 3 : now check does any other have already assigned random generated port
	 */
	final private boolean checkPortIsFree(JFrame frame) {
		String query = "SELECT `id` FROM `user_port` WHERE `port`=?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userPort.getPort());
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't set port", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
		return true;
	}
	
	/*
	 * Step 4
	 */
	final private void savePortToDatabase(JFrame frame) {
		String query = "INSERT INTO `user_port`(`id`, `ip`, `port`) VALUES (?,?,?)";
		try {
			getHostIp(frame);
			// Setting port number
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userPort.getId());
			preparedStatement.setString(2, userPort.getIp());
			preparedStatement.setString(3, userPort.getPort());
			if (preparedStatement.executeUpdate() == 0) {
				JOptionPane.showMessageDialog(null, "Can't set port number");
			}
		} catch (Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Can't set port number");
		}
	}
	
	/*
	 * Step 4.a
	 */

	final private void getHostIp(JFrame frame) {
		userPort.setIp("127.0.0.1");
		return;
//		String getServerIpQuery = "SELECT `ip` FROM `user_port` WHERE id=10000000";
//		try {
//			preparedStatement = connection.prepareStatement(getServerIpQuery);
//			resultSet = preparedStatement.executeQuery();
//			resultSet.next();
//			userPort.setIp(resultSet.getString(1));
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(frame, "No server online");
//		}
	}
	/*
	 * Initialization of port is end
	 */

	/*
	 * get all port details from id
	 * @id
	 * @ip
	 * @for_id
	 * @port
	 */
	final public void setUserPort(JFrame frame) {
		String query = "SELECT `ip`,`for_id`,`port` from `user_port` WHERE `id` = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			userPort = new UserPort();
			userPort.setId(id);
			userPort.setIp(resultSet.getString(1));
			userPort.setForId(resultSet.getString(2));
			userPort.setPort(resultSet.getString(3));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't set chating with", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/*
	 * to make user busy with friend chat
	 */
	final public void setWaitingFor(String friendId, JFrame frame) {
		String query = "UPDATE `user_port` SET `for_id`=? WHERE `id`=?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, friendId);
			preparedStatement.setString(2, userPort.getId());
			if (preparedStatement.executeUpdate() == 0) {
				JOptionPane.showMessageDialog(frame, "Can't set chating with option", "Oops !",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't set chating with", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
