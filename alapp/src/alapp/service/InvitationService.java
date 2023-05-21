package alapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alapp.config.DatabaseConfig;
import alapp.model.User;

public class InvitationService {
	private PreparedStatement preparedStatement;
	private Connection connection;
	private ResultSet resultSet;

	private User user;
	private JFrame frame;

	public InvitationService(JFrame frame, User user) {
		this.user = user;
		this.frame = frame;
		connection = DatabaseConfig.getConnection();
	}

	public DefaultListModel<User> getSendRequestsListModel() {
		DefaultListModel<User> sendRequestListModel = new DefaultListModel<>();
		try {
			String query = "SELECT "
					+ "`user_information`.`id`, "
					+ "`user_information`.`first_name`, "
					+ "`user_information`.`last_name`, "
					+ "`user_information`.`username`,"
					+ "`user_information`.`active` "
					+ "FROM "
						+ "`user_information` "
					+ "INNER JOIN `invitation` "
						+ "ON `user_information`.id = `invitation`.to_id "
					+ "WHERE `invitation`.`from_id`=?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getId());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				sendRequestListModel.addElement(new User(resultSet.getString(1), resultSet.getString(2),
						resultSet.getString(3), resultSet.getString(4), null, resultSet.getString(5), null));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "There's some error in getting data", "Oops !",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return sendRequestListModel;
	}

	public DefaultListModel<User> getReceivedRequestsListModel() {
		DefaultListModel<User> receivedRequestListModel = new DefaultListModel<>();
		String query = "SELECT `user_information`.`id`, `user_information`.`first_name`,`user_information`.`last_name`,`user_information`.`username`,`user_information`.`active` FROM `user_information` INNER JOIN `invitation` ON `user_information`.`id` = `invitation`.`from_id` WHERE `invitation`.`to_id` = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getId());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				receivedRequestListModel.addElement(new User(resultSet.getString(1), resultSet.getString(2),
						resultSet.getString(3), resultSet.getString(4), null, resultSet.getString(5), null));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "There's some error in getting data", "Oops !",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return receivedRequestListModel;
	}

	public int deleteRequest(User from, User to) {
		String query = "DELETE FROM `invitation` WHERE `to_id`=? AND `from_id`=?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, to.getId());
			preparedStatement.setString(2, from.getId());
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't respond to request", "Error !",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return 0;
	}

	public boolean isInvitationExist(JFrame frame, User user, User searchUser) {
		String from_idQuery = "SELECT `from_id`, `to_id` FROM `invitation` WHERE `from_id` = ? AND `to_id` = ?";
		try {
			preparedStatement = connection.prepareStatement(from_idQuery);
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, searchUser.getId());
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't Search for user", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public void addRequest(JFrame frame, User searchUser) {
		String query = "INSERT INTO `invitation`(`from_id`, `to_id`) VALUES (?,?)";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, searchUser.getId());
			if (preparedStatement.executeUpdate() != 0) {
				JOptionPane
						.showMessageDialog(frame,
								"Request sent to " + searchUser.getUsername() + "(" + searchUser.getFirstName() + " "
										+ searchUser.getLastName() + ")",
								"Invitation sent !", JOptionPane.DEFAULT_OPTION);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error in invitation", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
