package alapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alapp.config.DatabaseConfig;
import alapp.model.User;

public class ConnectionService {
	private PreparedStatement preparedStatement;
	private Connection connection;
	private ResultSet resultSet;

	private User user;

	public ConnectionService(User user) {
		this.user = user;
		preparedStatement = null;
		connection = DatabaseConfig.getConnection();
	}

	final public boolean removeConnection(String user1Id, String user2Id, JFrame frame) {
		String query = "DELETE FROM `connections` WHERE (`to_id`=? and `from_id`=?) OR (`to_id`=? and `from_id`=?)";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user1Id);
			preparedStatement.setString(2, user2Id);
			preparedStatement.setString(3, user2Id);
			preparedStatement.setString(4, user1Id);
			return (preparedStatement.executeUpdate() != 0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't Unfriend");
		}
		return false;
	}

	public boolean isConnectionExist(JFrame frame, User searchUser) {
		String from_idQuery = "SELECT * FROM `connections` WHERE `from_id` = ? AND `to_id` = ?";
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

	public int acceptRequest(JFrame frame, User requestSenderUser) {
		String query = "INSERT INTO `connections`(`from_id`, `to_id`) VALUES (?,?),(?,?)";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, requestSenderUser.getId());
			preparedStatement.setString(3, requestSenderUser.getId());
			preparedStatement.setString(4, user.getId());
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't respond to request", "Error !",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return 0;
	}
}
