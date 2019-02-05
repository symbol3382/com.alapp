package alapp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;

public class SelectChatPanel extends JPanel {
	Connection connection;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	
	String selectedFriendId;
	
	FriendDetailPanel fdp;
	
	JLabel lblChatWith;
	DefaultComboBoxModel<String> dcbmFriendsList;
	DefaultComboBoxModel<String> dcbmFriendsListId;
	JComboBox<String> cbFriendList;
	JButton btnRefresh;
	
	public SelectChatPanel(FriendDetailPanel fdp) {
		connection = DBInfo.getConnection();
		this.fdp = fdp;
		setLayout(null);
		declareComponents();
		setBackgroundColor();
		setColor();
		refreshSelectChatPanel();
		
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshSelectChatPanel();
			}
		});
		cbFriendList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fdp.refereshFriendPanel(dcbmFriendsListId.getElementAt(cbFriendList.getSelectedIndex()));
			}
		});
	}
	
	void declareComponents() {
		lblChatWith = new JLabel("Chat With");
		lblChatWith.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		dcbmFriendsList = new DefaultComboBoxModel<String>();
		dcbmFriendsListId = new DefaultComboBoxModel<String>();
		cbFriendList = new JComboBox<String>(dcbmFriendsList);
		btnRefresh = new JButton("");

		lblChatWith.setBounds(10, 8, 50, 14);
		cbFriendList.setBounds(70, 4, 372, 22);
		btnRefresh.setBounds(452, 4, 22, 22);

		add(lblChatWith);
		add(cbFriendList);
		add(btnRefresh);
	}
	/*
	 * set background to null
	 */
	private void setBackgroundColor() {
		setBackground(null);
		btnRefresh.setBackground(null);
	}
	/*
	 * set color for UI Design
	 */
	void setColor() {
		lblChatWith.setForeground(UI.getTextColor());
		cbFriendList.setBackground(UI.getComponentColor());
		cbFriendList.setForeground(UI.getTextColor());
		btnRefresh.setForeground(UI.getTextColor());
		fdp.setColor();
	}
	/*
	 * To get friends list from connections
	 */
	void refreshSelectChatPanel(){
		String query = "SELECT `user_information`.`first_name`, `user_information`.`last_name`, `user_information`.`id` FROM `user_information` INNER JOIN `connections` ON `user_information`.`id` = `connections`.`to_id` WHERE `connections`.`from_id` = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,DBMethod.userId);
			resultSet = preparedStatement.executeQuery();
			dcbmFriendsList.removeAllElements();
			dcbmFriendsListId.removeAllElements();
			while(resultSet.next()) {
				dcbmFriendsListId.addElement(resultSet.getString(3));
				dcbmFriendsList.addElement(resultSet.getString(1) + " " + resultSet.getString(2));
			}
			selectedFriendId = dcbmFriendsListId.getElementAt(cbFriendList.getSelectedIndex());
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Can't Get Friend List", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		if(dcbmFriendsList.getSize() > 0)
			fdp.refereshFriendPanel(dcbmFriendsListId.getElementAt(0));
	}
	/*
	 * Intercommunication between SelectChatPanel <-> FriendDetailPanel
	 */
	int getFriendsCount() {
		return dcbmFriendsList.getSize();
	}
}

