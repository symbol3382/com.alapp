package alapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alapp.config.DatabaseConfig;
import alapp.model.User;

public class FriendService {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	private User user;
	JFrame frame;
	
	public FriendService(User user, JFrame frame) {
		this.user = user;
		this.frame = frame;
		connection = DatabaseConfig.getConnection();
	}
	
	public DefaultComboBoxModel<User> listFriends(JFrame frame){
		DefaultComboBoxModel<User> dcbm = new DefaultComboBoxModel<User>();

		String query = "SELECT "
				+ "`user_information`.`id`, "
				+ "`user_information`.`first_name`, "
				+ "`user_information`.`last_name`, "
				+ "`user_information`.`username`, "
				+ "`user_information`.`active` "
				+ "FROM "
					+ "`user_information` "
				+ "INNER JOIN `connections` "
				+ "ON `user_information`.`id` = `connections`.`to_id` "
				+ "WHERE `connections`.`from_id` = ?";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,user.getId());
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				dcbm.addElement(new User(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),null,resultSet.getString(5),null));
			}
		}catch(Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(frame, "Can't Get Friend List", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		return dcbm;
	}
	
	
}
