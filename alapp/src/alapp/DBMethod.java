package alapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DBMethod {
	static PreparedStatement preparedStatement;
	static Connection connection;
	static ResultSet resultSet;
	
	static String userId;
	static String userName;
	static String hostIp;
	static String userPort;
	
	/*
	 * For getting details of friend to check
	 * ip address -> for as client
	 * forId ->	to check free or waiting for 
	 * port -> to get the port of user for as client purpose
	 */
	
	static String friendIP;
	static String friendForId;
	static int friendPort;
	
	static String friendId;
	static String friendFirstName;
	static String friendLastName;
	static String friendUsername;
	static String friendActive;
	
	/**
	 * for distinguish between start panel as server or client
	 */
	final static int SERVER_CHAT_PANEL = 1;
	final static int CLIENT_CHAT_PANEL = 2;
	
	public final static int HOST_SERVER = 1;
	public final static int HOST_NULL = 0;
	
	static {
		preparedStatement = null;
		connection = DBInfo.getConnection();
	}
	
	final static void exit(JFrame frame) {
		String query = "UPDATE `user_information` SET `active`=0 WHERE `id` = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error in setting status offline", "Can't get offline", JOptionPane.ERROR_MESSAGE);
		}finally {
			try {
				DBInfo.closeConnection();
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(frame, "Error MEssage", "Error in closing conneciton", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
/*
 * Port Area
 */
	/*
	 * To free port on exit or logout
	 */
	final static void freeIPPort(JFrame frame) {
		String query = "DELETE FROM `user_port` WHERE `id`=?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.executeUpdate();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(frame, "Can't free ports", "Erroe", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * get port numbers (unique)
	 */
	final static void initializePortNumber(JFrame frame) {
		userPort = generatePortNumber();
		freeIPPort(frame);
		if(checkPortNumber(userPort,frame)) {
			setPortNumber(userPort,frame);
		}
		else {
			initializePortNumber(frame);
		}
	}
	final static String generatePortNumber() {
		String port = "";
		for(int i=0;i<5;i++) {
			port = port + (int)(Math.random() * 10);
		}
		return port;
	}
	final static boolean checkPortNumber(String port,JFrame frame) {
		String query = "SELECT `id` FROM `user_port` WHERE `port`=?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, port);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Can't set port", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
		return true;
	}
	final static void setPortNumber(String port,JFrame frame) {
		String query = "INSERT INTO `user_port`(`id`, `ip`, `port`) VALUES (?,?,?)";
		try {
			getHostIp(frame);
			// Setting port number 
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, hostIp);
			preparedStatement.setString(3, port);
			if(preparedStatement.executeUpdate()==0) {
				JOptionPane.showMessageDialog(null, "Can't set port number");
			}
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Can't set port number");
		}
	}
	/*
	 * For getting ip of host
	 */
	final static void getHostIp(JFrame frame) {
		String getServerIpQuery = "SELECT `ip` FROM `user_port` WHERE id=10000000";
		try {
			preparedStatement = connection.prepareStatement(getServerIpQuery);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			hostIp = resultSet.getString(1);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame, "No server online");
		}
	}
/*
 * End of port area
 */
	/*
	 * Remove friends or connection
	 */
	final static boolean removeConnection(String user1Id,String user2Id,JFrame frame) {
		String query = "DELETE FROM `connections` WHERE (`to_id`=? and `from_id`=?) OR (`to_id`=? and `from_id`=?)";
		try {
			preparedStatement = DBMethod.connection.prepareStatement(query);
			preparedStatement.setString(1, user1Id);
			preparedStatement.setString(2, user2Id);
			preparedStatement.setString(3, user2Id);
			preparedStatement.setString(4, user1Id);
			return (preparedStatement.executeUpdate() != 0);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame,  "Can't Unfriend");
		}
		return false;
	}
	/*
	 * To set the user active
	 */
	final static void setStatusActive(JFrame frame){
		try {
			String query = "UPDATE `user_information` SET `active`=1 WHERE `id` = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.executeUpdate();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame, "There's some error in getting data", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * to make user busy with friend chat
	 */
	final static void setWaitingFor(String friendId,JFrame frame) {
		String query = "UPDATE `user_port` SET `for_id`=? WHERE `id`=?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, friendId);
			preparedStatement.setString(2, userId);
			if(preparedStatement.executeUpdate()==0) {
				JOptionPane.showMessageDialog(frame, "Can't set chating with option", "Oops !", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't set chating with", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * To send the ip of server
	 */
	final static void setUserPortDetails(String friendId, JFrame frame) {
		String query = "SELECT `ip`,`for_id`,`port` from `user_port` WHERE `id` = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, friendId);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			friendIP = resultSet.getString(1);
			friendForId = resultSet.getString(2);
			friendPort = Integer.valueOf(resultSet.getString(3));
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't set chating with", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * to set the details of friend to friend details
	 */
	final static void getUserDetails(String friendId,JFrame frame) {
		String query = "SELECT `first_name`, `last_name`, `username`, `active` FROM `user_information` WHERE `id`=?";
		try {
			preparedStatement = DBMethod.connection.prepareStatement(query);
			preparedStatement.setString(1, friendId);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				friendFirstName = resultSet.getString(1);
				friendLastName = resultSet.getString(2);
				friendUsername = resultSet.getString(3);
				friendActive = resultSet.getString(4);
			}
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Can't get user details");
		}
	}
}
