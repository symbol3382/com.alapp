package alapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alapp.config.DatabaseConfig;
import alapp.model.User;

public class UserService {

	private PreparedStatement preparedStatement;
	private Connection connection;
	private ResultSet resultSet;

	/*
	 * New Area
	 */

	private User user;

	public User getUser() {
		return user;
	}

	/*
	 * UserRegistration ActivityMain
	 */
	public UserService() {
		preparedStatement = null;
		connection = DatabaseConfig.getConnection();
	}

	/**
	 * To check for user exist or not so that login can be validated
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public String checkUserForLogin(String username, String password) {
		String query = "SELECT `id`, `first_name`, `last_name`, `username`, `active`, `user_type` FROM `user_information` WHERE `username` = ? AND `password` = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				user = new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), password, resultSet.getString(5), resultSet.getString(6));
				return resultSet.getString(6);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * To set the user active(online)
	 */
	final public void setStatusActive(JFrame frame) {
		try {
			String query = "UPDATE `user_information` SET `active`=1 WHERE `id` = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "There's some error in getting data", "Oops !",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/*
	 * The exit function
	 */

	final public void exit(JFrame frame) {
		String query = "UPDATE `user_information` SET `active`=0 WHERE `id` = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error in setting status offline", "Can't get offline",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				DatabaseConfig.closeConnection();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "Error MEssage", "Error in closing conneciton",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public User isUserExist(JFrame frame, String username) {
		String from_idQuery = "SELECT `id`, `first_name`, `last_name`, `username`, `active` from `user_information` WHERE `username`=?";
		try {
			preparedStatement = connection.prepareStatement(from_idQuery);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), null, resultSet.getString(5), null);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't Search for user", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public boolean addUser(User user) {
		String query = "INSERT INTO `user_information` (`first_name`, `last_name`, `username`, `password`) VALUES (?, ?, ?, ?);";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getFirstName()); // FirstName
			preparedStatement.setString(2, user.getLastName()); // Last name
			preparedStatement.setString(3, user.getUsername()); // Username
			preparedStatement.setString(4, user.getPassword()); // Password
			if (preparedStatement.executeUpdate() == 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * End of new area
	 */
}
