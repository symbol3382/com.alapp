package alapp;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.awt.event.ActionEvent;

public class FriendDetailPanel extends JPanel {

	PreparedStatement preparedStatement;
	ResultSet resultSet;
	
	SelectChatPanel scp;
	JFrame frame;
	ChatPanel chatPanel;
	
	private String friendId;
	
	JLabel lblFriendName;
	JLabel lblUsername;
	JLabel lblStatus;
	JLabel lblCurrentStatusIcon;
	JLabel lblCurrentStatus;
	
	private JButton btnStartChat;
	private JButton btnUnfriend;
	
	DataOutputStream dos;
	DataInputStream din;

	
	public FriendDetailPanel(JFrame frame) {
		this.frame = frame;
		declareComponents();
		setBackgroundColor();
		setLayout(null);
		setBounds(0,0,484,122);
		add(lblCurrentStatusIcon);
		add(lblCurrentStatus);
		add(lblStatus);
		add(lblFriendName);
		add(lblUsername);
		add(btnStartChat);
		add(btnUnfriend);	
		
		btnUnfriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				unfriend();
			}
		});
		btnStartChat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startChat();
			}
		});
	}
	private void declareComponents() {
		lblFriendName = new JLabel("Select friend to chat");
		lblUsername = new JLabel("");
		lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		lblCurrentStatusIcon = new JLabel("");
		lblCurrentStatusIcon.setIcon(new ImageIcon("C:\\Users\\symbol\\Documents\\RAT\\alapp\\assets\\online_status.png"));
		lblCurrentStatus = new JLabel("");
		lblCurrentStatus.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		btnStartChat = new JButton("Start Chat");
		btnUnfriend = new JButton("Unfriend");
		lblFriendName.setBounds(10, 10, 250, 30);
		lblUsername.setBounds(10, 49, 151, 14);
		lblStatus.setBounds(10, 74, 31, 14);
		lblCurrentStatus.setBounds(65, 74, 89, 14);
		lblCurrentStatusIcon.setBounds(51, 74, 10, 14);
		btnStartChat.setBounds(385, 43, 89, 68);
		btnUnfriend.setBounds(385, 9, 89, 23);
		lblFriendName.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
		lblUsername.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		btnStartChat.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		btnUnfriend.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
	}
	void setBackgroundColor() {
		setBackground(null);
		btnStartChat.setBorder(null);
		btnUnfriend.setBackground(null);
		btnStartChat.setForeground(Color.WHITE);
	}
	void setColor() {
		btnUnfriend.setForeground(UI.getTextColor());
		btnStartChat.setBackground(UI.getThemeColor());
		lblStatus.setForeground(UI.getTextColor());
		lblCurrentStatus.setForeground(UI.getTextColor());
		lblFriendName.setForeground(UI.getTextColor());
		lblUsername.setForeground(UI.getTextColor());
	}
	void refereshFriendPanel(String friendId) {
		this.friendId = friendId;
		DBMethod.getUserDetails(friendId, frame);
		setUserDetails();
	}
	private void setUserDetails() {
		lblFriendName.setText(DBMethod.friendFirstName + " " + DBMethod.friendLastName);
		lblUsername.setText(DBMethod.friendUsername);
		if(DBMethod.friendActive.equals("1")) {
			lblCurrentStatusIcon.setIcon(new ImageIcon("C:\\Users\\symbol\\Documents\\RAT\\alapp\\assets\\online_status.png"));
			lblCurrentStatus.setText("(online)");
		}
		else {
			lblCurrentStatusIcon.setIcon(new ImageIcon("C:\\Users\\symbol\\Documents\\RAT\\alapp\\assets\\offline_status.png"));
			lblCurrentStatus.setText("(offline)");
		}
	}
	/*
	 * Unfriend
	 */
	private void unfriend() {
		if(scp.getFriendsCount() ==0 ) {
			JOptionPane.showMessageDialog(this, "You have no friend", "No Friend", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			int confirm = JOptionPane.showConfirmDialog(this, "You will no longer friend with " + DBMethod.friendUsername);
			if(confirm == 0) {
				if(DBMethod.removeConnection(DBMethod.userId,friendId,frame)) {
					JOptionPane.showMessageDialog(this,  "You are no longer friend with " + DBMethod.friendUsername);				
					scp.refreshSelectChatPanel();
					if(scp.getFriendsCount() == 0) {
						lblFriendName.setText("Select friend to chat");
						lblUsername.setText("");
						lblCurrentStatus.setText("");
						lblCurrentStatusIcon.setIcon(null);
					}
				}
			}
		}
	}
	/*
	 * get SelectChatPanel for refresh after unfriend
	 */
	void setSelectChatPanel(SelectChatPanel scp){
		this.scp = scp;
	}
	/*
	 * start chat with friend
	 */
	void startChat() {
		scp.refreshSelectChatPanel();
		if(scp.getFriendsCount() == 0) {
			JOptionPane.showMessageDialog(this, "Please add a new friend to chat", "No Friend", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			if(DBMethod.friendActive.equals("1")) {
				frame.setBounds(frame.getX(), frame.getY(), 500, 609);
				startChatPanel();
			}
			else {
				JOptionPane.showMessageDialog(this, DBMethod.friendUsername + " is offline");
			}
		}
	}
	void startChatPanel() {
		DBMethod.setUserPortDetails(friendId,frame);
		DBMethod.setWaitingFor(friendId,frame);
		/*
		 * Check friend is busy or not
		 */
		if(DBMethod.friendForId.equals(DBMethod.userId)) {
			// Friend is already waiting for this user
			// Start chat as client
			chatPanel = new ChatPanel(DBMethod.CLIENT_CHAT_PANEL);
		}
		else if(DBMethod.friendForId.equals("0")) {
			// Friend is free and waiting for no one 
			// Start chat as server
			chatPanel = new ChatPanel(DBMethod.SERVER_CHAT_PANEL);
		}
		else {
			// Friend is busy with some one and 
			// Don't start the chat as friend is busy with another
			chatPanel = new ChatPanel(DBMethod.SERVER_CHAT_PANEL);
			JOptionPane.showMessageDialog(this, DBMethod.friendUsername + " is busy your message will not be send", "Busy !", JOptionPane.INFORMATION_MESSAGE);
		}
		chatPanel.setBounds(0,170,484,400);
		frame.getContentPane().add(chatPanel);
		chatPanel.startChat();
	}
}
