package alapp.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import alapp.config.UIColorConfig;
import alapp.model.User;
import alapp.service.ConnectionService;
import alapp.service.InvitationService;
import alapp.service.UserService;

public class UserMenuBarPanel extends JPanel {

	/* UI Components */
	JMenuBar menuBar;
	JMenu mnUsername;
	JMenu mnAdd;
	JMenu mnRequest;
	JMenu mnSend;
	JMenu mnRecieved;
	JMenu mnChangeTheme;
	JMenuItem mntmDarkMode;
	JMenuItem mntmLightMode;
	JMenuItem mntmLogOut;
	JMenuItem mntmExit;
	JMenuItem mntmAddFriend;
	JMenu mnPreference;
	JMenuItem mntmChangeIp;
	JMenuItem mntmChangePort;
	JList<User> listRequestReceive;
	JList<User> listRequestSend;

	JFrame frame;

	UserChatSelectPanel userChatSelectPanel;

	/*
	 * Business Logic Service variables
	 */
	private User user;
	private User searchUser;
	private UserService userService;
	private ConnectionService connectionService;
	private InvitationService invitationService;
	/* Component data */
	private DefaultListModel<User> receivedRequestListModel;
	private DefaultListModel<User> sendRequestListModel;

	public UserMenuBarPanel(JFrame frame, UserService userService, UserChatSelectPanel userChatSelectPanel) {
		this.frame = frame;
		this.user = userService.getUser();
		this.userService = userService;
		this.userChatSelectPanel = userChatSelectPanel;
		invitationService = new InvitationService(frame, user);
		connectionService = new ConnectionService(user);
		
		setLayout(null);
		declareComponents();
		addItem();
		setBackgroundColor();
		setColor();
		refreshMenuBarPanel();

		/*
		 * Dark Mode
		 */
		mntmDarkMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UIColorConfig.darkTheme();
				setColor();
			}
		});
		/*
		 * Light mode
		 */
		mntmLightMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UIColorConfig.lightTheme();
				setColor();
			}
		});

		/*
		 * Add Friend
		 */
		mntmAddFriend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendRequest();
			}
		});
		/*
		 * Refreshing list of requests
		 */
		mnRequest.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent arg0) {
				refreshMenuBarPanel();
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
			}
		});
		/*
		 * Request respondSentRequest
		 */
		listRequestSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				respondSendRequest();
			}
		});
		/*
		 * Request received
		 */
		listRequestReceive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				respondReceivedInvite();
				userChatSelectPanel.refreshFriendList();
			}
		});
	}

	private void declareComponents() {
		menuBar = new JMenuBar();
		mnUsername = new JMenu(user.getUsername()); // User name
		mnPreference = new JMenu("Preference");
		mnChangeTheme = new JMenu("Change Theme");
		mntmDarkMode = new JMenuItem("Dark Mode");
		mntmLightMode = new JMenuItem("Light Mode");
		mntmChangeIp = new JMenuItem("Change IP");
		mntmChangePort = new JMenuItem("Change Port");
		mntmLogOut = new JMenuItem("Log out");
		mntmExit = new JMenuItem("Quit");
		mnAdd = new JMenu("Add"); // Add Menu
		mntmAddFriend = new JMenuItem("Friend");
		mnRequest = new JMenu("Request"); // Request Menu
		mnSend = new JMenu("Send");
		mnRecieved = new JMenu("Received");

		sendRequestListModel = new DefaultListModel<User>();
		receivedRequestListModel = new DefaultListModel<User>();
		listRequestReceive = new JList<User>(receivedRequestListModel);
		listRequestSend = new JList<User>(sendRequestListModel);
		listRequestReceive.setBorder(new EmptyBorder(0, 15, 0, 15));
		listRequestSend.setBorder(new EmptyBorder(0, 15, 0, 15));
	}

	private void addItem() {
		menuBar.add(mnUsername); /* Adding menu to menu bar */
		mnUsername.add(mnPreference);
		mnPreference.add(mnChangeTheme);
		mnChangeTheme.add(mntmDarkMode);
		mnChangeTheme.add(mntmLightMode);
		mnPreference.add(mntmChangePort);
		mnPreference.add(mntmChangeIp);
		mnUsername.add(mntmLogOut); /* Adding menu items to menu */
		mnUsername.add(mntmExit);
		menuBar.add(mnAdd);
		mnAdd.add(mntmAddFriend);
		menuBar.add(mnRequest);
		mnRequest.add(mnSend);
		mnSend.add(listRequestSend);
		mnRequest.add(mnRecieved);
		mnRecieved.add(listRequestReceive);
		menuBar.setBounds(0, 0, 500, 20);
		add(menuBar);
	}

	/*
	 * For null background
	 */
	private void setBackgroundColor() {
		setBackground(null);
		menuBar.setBackground(null);
		mnPreference.setOpaque(true);
		mnChangeTheme.setOpaque(true);
		mnSend.setOpaque(true);
		mnRecieved.setOpaque(true);
	}

	/*
	 * For setting color
	 */
	private void setColor() {
		frame.getContentPane().setBackground(UIColorConfig.getWallColor());
		menuBar.setForeground(UIColorConfig.getTextColor());
		mnUsername.setForeground(UIColorConfig.getTextColor());
		mnPreference.setForeground(UIColorConfig.getTextColor());
		mnChangeTheme.setForeground(UIColorConfig.getTextColor());
		mntmDarkMode.setForeground(UIColorConfig.getTextColor());
		mntmLightMode.setForeground(UIColorConfig.getTextColor());
		mntmChangePort.setForeground(UIColorConfig.getTextColor());
		mntmChangeIp.setForeground(UIColorConfig.getTextColor());
		mntmLogOut.setForeground(UIColorConfig.getTextColor());
		mntmExit.setForeground(UIColorConfig.getTextColor());
		mnAdd.setForeground(UIColorConfig.getTextColor());
		mntmAddFriend.setForeground(UIColorConfig.getTextColor());
		mnRequest.setForeground(UIColorConfig.getTextColor());
		mnSend.setForeground(UIColorConfig.getTextColor());
		listRequestReceive.setForeground(UIColorConfig.getTextColor());
		mnRecieved.setForeground(UIColorConfig.getTextColor());
		listRequestSend.setForeground(UIColorConfig.getTextColor());

		mnUsername.setBackground(UIColorConfig.getComponentColor());
		mnPreference.setBackground(UIColorConfig.getComponentColor());
		mnChangeTheme.setBackground(UIColorConfig.getComponentColor());
		mntmDarkMode.setBackground(UIColorConfig.getComponentColor());
		mntmLightMode.setBackground(UIColorConfig.getComponentColor());
		mntmChangeIp.setBackground(UIColorConfig.getComponentColor());
		mntmChangePort.setBackground(UIColorConfig.getComponentColor());
		mntmLogOut.setBackground(UIColorConfig.getComponentColor());
		mntmExit.setBackground(UIColorConfig.getComponentColor());
		mntmAddFriend.setBackground(UIColorConfig.getComponentColor());
		mnSend.setBackground(UIColorConfig.getComponentColor());
		mnRecieved.setBackground(UIColorConfig.getComponentColor());
		listRequestSend.setBackground(UIColorConfig.getComponentColor());
		listRequestReceive.setBackground(UIColorConfig.getComponentColor());
	}

	/*
	 * For refreshing the menu bar items
	 */
	public void refreshMenuBarPanel() {
		try {
			// Request Send
			sendRequestListModel = invitationService.getSendRequestsListModel();
			mnSend.setText("Send (" + sendRequestListModel.getSize() + ")");
			listRequestSend.setModel(sendRequestListModel);
			// Request Received
			receivedRequestListModel = invitationService.getReceivedRequestsListModel();
			mnRecieved.setText("Received (" + receivedRequestListModel.getSize() + ")");
			listRequestReceive.setModel(receivedRequestListModel);
		} catch (Exception e) {
			System.out.println("error in refresh Menu Bar");
			System.out.println(e.getStackTrace());

		}
	}

	/*
	 * Respond to send request for canceling sent request
	 */
	private void respondSendRequest() {
		int request = JOptionPane.showConfirmDialog(frame,
				"Do you wanna cancel send request to " + listRequestSend.getSelectedValue().getUsername() + "("
						+ listRequestSend.getSelectedValue().getFirstName() + " "
						+ listRequestSend.getSelectedValue().getLastName() + ")");

		if (request == 0) {
			if (invitationService.deleteRequest(user, listRequestSend.getSelectedValue()) != 0) {
				JOptionPane.showMessageDialog(frame, "Request Cancelled", "Done !", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		refreshMenuBarPanel();

	}

	/*
	 * Respond to received request
	 * 
	 * @event when user click on user name from received request list
	 */
	private void respondReceivedInvite() {
		String[] options = { "Accept", "Reject" };
		String name = listRequestReceive.getSelectedValue().getUsername() + "("
				+ listRequestReceive.getSelectedValue().getFirstName()
				+ listRequestReceive.getSelectedValue().getLastName() + ")";
		int request = JOptionPane.showOptionDialog(frame, name + " wants to make a connection with you",
				"Respond Reqeust", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (request == 1) { // Invitation rejected
			if (invitationService.deleteRequest(listRequestReceive.getSelectedValue(), user) != 0) {
				JOptionPane.showMessageDialog(frame, "Request Cancelled", "Done !", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "Request can't cancelled", "Error !",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (request == 0) { // Invitation accepted
			if (invitationService.deleteRequest(listRequestReceive.getSelectedValue(), user) != 0) {
				if (connectionService.acceptRequest(frame, listRequestReceive.getSelectedValue()) != 0) {
					JOptionPane.showMessageDialog(frame, "You are now friend with " + name, "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(frame, "Can't respond to request", "Error !",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		refreshMenuBarPanel();
	}

	/*
	 * Search friend and send request check for user is exist or not then check for
	 * already friend already request send already request come from searching user
	 */
	private void sendRequest() {
		String searchName = JOptionPane.showInputDialog(frame, "Enter username to search ");
		if (searchName != null) {
			if (searchName.equals("") || searchName == null) {
				JOptionPane.showMessageDialog(frame, "Enter name to search", "Empty Name",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (searchName.equals(user.getUsername())) {
				JOptionPane.showMessageDialog(frame, "You are already friend with yourself", "Invalid search",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ((searchUser = userService.isUserExist(frame, searchName)) != null) {
					if (!connectionService.isConnectionExist(frame, searchUser)) {
						if (!invitationService.isInvitationExist(frame, user, searchUser)) {
							if (!invitationService.isInvitationExist(frame, searchUser, user)) {
								invitationService.addRequest(frame, searchUser);
							} else { // Already request received
								JOptionPane.showMessageDialog(frame, "You already have reequest from " + searchName,
										"Check Requests !", JOptionPane.DEFAULT_OPTION);
							}
						} else { // Already request sent
							JOptionPane.showMessageDialog(frame, "Request already sent to " + searchName, "Wait !",
									JOptionPane.DEFAULT_OPTION);
						}
					} else { // Already friend
						JOptionPane.showMessageDialog(frame, "You already friend with " + searchName, "Already Friends",
								JOptionPane.DEFAULT_OPTION);
					}
				} else { // No user found
					JOptionPane.showMessageDialog(frame, "No user found", "404", JOptionPane.INFORMATION_MESSAGE);
				} // isUserExist
			} // search name not same as username
		} // search name is not empty
	}

}
