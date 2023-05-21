package alapp.panel;

import javax.swing.JPanel;

import alapp.config.UIColorConfig;
import alapp.model.User;
import alapp.service.AppService;
import alapp.service.ConnectionService;
import alapp.service.UserPortService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.FontFormatException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class UserFriendDetailPanel extends JPanel {

	/*
	 * UI Components
	 */
	JLabel lblFriendName;
	JLabel lblUsername;
	JLabel lblStatus;
	JLabel lblCurrentStatusIcon;
	JLabel lblCurrentStatus;

	private JButton btnStartChat;
	private JButton btnUnfriend;

	private JFrame frame;
	private ChatPanel chatPanel;

	/*
	 * Additional component
	 */
	UserChatSelectPanel chatSelectPanel;

	/*
	 * Business Logic Service Components
	 */
	User user;
	UserPortService userPortService;

	User friendUser;
	UserPortService friendUserPortService;
	ConnectionService connectionService;

	/*
	 * For chatting panel data
	 */
	final int SERVER_CHAT_PANEL = 1;
	final int CLIENT_CHAT_PANEL = 2;

	DataOutputStream dos;
	DataInputStream din;

	public UserFriendDetailPanel(JFrame frame, User user, UserPortService userPortService,
			UserChatSelectPanel chatSelectPanel) {
		this.frame = frame;
		this.chatSelectPanel = chatSelectPanel;
		this.user = user;
		this.userPortService = userPortService;
		this.connectionService = new ConnectionService(user);
		declareComponents();
		setBackgroundColor();
		setColor();
		setLayout(null);
		setBounds(0, 0, 484, 122);
		add(lblCurrentStatusIcon);
		add(lblCurrentStatus);
		add(lblStatus);
		add(lblFriendName);
		add(lblUsername);
		add(btnStartChat);
		add(btnUnfriend);

		btnUnfriend.addActionListener(new ActionListener() {

			@Override
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

	/*
	 * Initialization
	 */
	private void declareComponents() {
		lblFriendName = new JLabel("Select friend to chat");
		lblUsername = new JLabel("");
		lblStatus = new JLabel("Status");
		lblStatus.setFont(AppService.getAppFont(11f));
		lblCurrentStatusIcon = new JLabel("");
		lblCurrentStatusIcon.setIcon(new ImageIcon("assets/online_status.png"));
		lblCurrentStatus = new JLabel("");
		lblCurrentStatus.setFont(AppService.getAppFont(11f));
		btnStartChat = new JButton("Start Chat");
		btnUnfriend = new JButton("Unfriend");
		lblFriendName.setBounds(10, 10, 250, 30);
		lblUsername.setBounds(10, 49, 151, 14);
		lblStatus.setBounds(10, 74, 31, 14);
		lblCurrentStatus.setBounds(65, 74, 89, 14);
		lblCurrentStatusIcon.setBounds(51, 74, 10, 14);
		btnStartChat.setBounds(385, 43, 89, 68);
		btnUnfriend.setBounds(385, 9, 89, 23);

		lblFriendName.setFont(AppService.getAppFont(24f));
		lblUsername.setFont(AppService.getAppFont(11f));
		btnStartChat.setFont(AppService.getAppFont(14f));
		btnUnfriend.setFont(AppService.getAppFont(11f));

	}

	public Font getAppFont() {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT,
					UserFriendDetailPanel.class.getResourceAsStream("assets/fonts/Helvetica.ttf"));
			return font;
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return null;

	}

	void setBackgroundColor() {
		setBackground(null);
		btnUnfriend.setBackground(null);
	}

	void setColor() {
		btnUnfriend.setForeground(UIColorConfig.getTextColor());
		btnStartChat.setBackground(UIColorConfig.getThemeColor());
		lblStatus.setForeground(UIColorConfig.getTextColor());
		lblCurrentStatus.setForeground(UIColorConfig.getTextColor());
		lblFriendName.setForeground(UIColorConfig.getTextColor());
		lblUsername.setForeground(UIColorConfig.getTextColor());
	}

	/*
	 * Initialization end
	 */

	void refereshFriendPanel() {
		if (chatSelectPanel.getDcbmFriendsList().getSize() == 0) {
			lblFriendName.setText("Select friend to chat");
			lblUsername.setText("");
			lblCurrentStatus.setText("");
			lblCurrentStatusIcon.setIcon(null);
		} else {
			friendUser = chatSelectPanel.getSelectedFriend();
			if (friendUser != null) {
				lblFriendName.setText(friendUser.getFirstName() + " " + friendUser.getLastName());
				lblUsername.setText(friendUser.getUsername());

				if (friendUser.getActive().equals("1")) {
					lblCurrentStatusIcon.setIcon(new ImageIcon("assets/online_status.png"));
					lblCurrentStatus.setText("(online)");
				} else {
					lblCurrentStatusIcon.setIcon(new ImageIcon("assets/offline_status.png"));
					lblCurrentStatus.setText("(offline)");
				}
			}
		}
	}

	/*
	 * Unfriend
	 */
	private void unfriend() {
		if (chatSelectPanel.getDcbmFriendsList().getSize() == 0) {
			JOptionPane.showMessageDialog(this, "You have no friend added in list", "404 - No Friends in list",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			int confirm = JOptionPane.showConfirmDialog(this,
					"You will no longer friend with " + friendUser.getUsername() + "(" + friendUser.getFirstName() + " "
							+ friendUser.getLastName() + ")");
			if (confirm == 0) {
				if (connectionService.removeConnection(user.getId(), friendUser.getId(), frame)) {
					JOptionPane.showMessageDialog(this, "You are no longer friend with " + friendUser.getUsername()
							+ "(" + friendUser.getFirstName() + " " + friendUser.getLastName() + ")");
				}
				chatSelectPanel.refreshFriendList();
			}
		}
	}

	/*
	 * start chat with friend
	 */
	void startChat() {
		chatSelectPanel.refreshFriendList();
		if (chatSelectPanel.getDcbmFriendsList().getSize() == 0) {
			JOptionPane.showMessageDialog(this, "Please add a new friend to chat", "No Friend",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			if (friendUser.getActive().equals("1")) {
				frame.setBounds(frame.getX(), frame.getY(), 500, 609);
				startChatPanel();
			} else {
				JOptionPane.showMessageDialog(this, friendUser.getUsername() + "(" + friendUser.getFirstName() + " "
						+ friendUser.getLastName() + ")" + " is offline");
			}
		}
	}

	void startChatPanel() {
		friendUserPortService = new UserPortService(friendUser.getId(), frame);

		friendUserPortService.setUserPort(frame);
		userPortService.setWaitingFor(friendUser.getId(), frame);

		/*
		 * Check friend is busy or not
		 */
		if (friendUserPortService.getUserPort().getForId() == null) {
			/*
			 * Friend is free and waiting for no one Start chat as server
			 */
			chatPanel = new ChatPanel(SERVER_CHAT_PANEL, userPortService);
		} else if (friendUserPortService.getUserPort().getForId().equals(userPortService.getUserPort().getId())) {
			/*
			 * Friend is already waiting for this user Start chat as client
			 */
			chatPanel = new ChatPanel(CLIENT_CHAT_PANEL, friendUserPortService);
		} else {
			/*
			 * Friend is busy with some one and Don't start the chat as friend is busy with
			 * another
			 */
			JOptionPane
					.showMessageDialog(this,
							friendUser.getUsername() + "(" + friendUser.getFirstName() + " " + friendUser.getLastName()
									+ ") is busy your message will not be send",
							"Busy !", JOptionPane.INFORMATION_MESSAGE);
		}
		chatPanel.setBounds(0, 170, 484, 400);
		frame.getContentPane().add(chatPanel);
		chatPanel.startChat();
	}
}
