package alapp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class MenuBarPanel extends JPanel {

	Connection connection;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	
	JFrame frame;
	
	/* String variables */
	String id;
	String userName;
	
	/* Components */
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
	JList<String> listRequestReceive;
	JList<String> listRequestSend;
	
	/* Component data */
	private DefaultListModel<String> receviedListArray;
	private DefaultListModel<String> sentListArray;

	public MenuBarPanel(String id,String userName,JFrame frame,SelectChatPanel scp) {
		this.id = id;
		this.userName = userName;
		this.frame = frame;
		connection = DBInfo.getConnection();
		setLayout(null);
		declareComponents();
		addItem();
		setBackgroundColor();
		setColor();
		refreshMenuBarPanel();
		/*
		 * Events
		 */
		/* User name menu
		 * 	Preference
		 * 		Change IP
		 * 		Change Port
		 * 	Logout
		 * 	Exit
		 */
		
		/*
		 * Dark Mode
		 */
		mntmDarkMode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UI.darkTheme();
				setColor();
				scp.setColor();
			}
		});
		/*
		 * Light mode
		 */
		mntmLightMode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UI.lightTheme();
				setColor();
				scp.setColor();
			}
		});
		/* 
		 * logout
		 */
		mntmLogOut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBMethod.freeIPPort(frame);
				DBMethod.exit(frame);
				new ActivityMain();
				ActivityMain.frame.setLocationRelativeTo(frame);
				ActivityMain.frame.setVisible(true);
				frame.setVisible(false);
				
			}
		});
		/*
		 * exit
		 */
		mntmExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBMethod.freeIPPort(frame);
				DBMethod.exit(frame);
				System.exit(0);
			}
		});
		/*
		 * Add
		 * 	Friend
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
				scp.refreshSelectChatPanel();
			}
		});
	}

	private void declareComponents() {
		menuBar = new JMenuBar();
		mnUsername = new JMenu(userName);					// User name
		mnPreference = new JMenu("Preference");
		mnChangeTheme = new JMenu("Change Theme");
		mntmDarkMode = new JMenuItem("Dark Mode");
		mntmLightMode = new JMenuItem("Light Mode");
		mntmChangeIp = new JMenuItem("Change IP");
		mntmChangePort = new JMenuItem("Change Port");
		mntmLogOut = new JMenuItem("Log out");
		mntmExit = new JMenuItem("Quit");
		mnAdd = new JMenu("Add");							// Add Menu
		mntmAddFriend = new JMenuItem("Friend");
		mnRequest = new JMenu("Request");					// Request Menu
		mnSend = new JMenu("Send");
		mnRecieved = new JMenu("Received");
			
		sentListArray = new DefaultListModel<>();
		receviedListArray = new DefaultListModel<>();
		listRequestReceive = new JList<String>(receviedListArray);
		listRequestSend = new JList<String>(sentListArray);
		listRequestReceive.setBorder(new EmptyBorder(0, 15, 0, 15));
		listRequestSend.setBorder(new EmptyBorder(0, 15, 0, 15));
	}
	private void addItem() {
		menuBar.add(mnUsername);	/* Adding menu to menu bar */
		mnUsername.add(mnPreference);
		mnPreference.add(mnChangeTheme);
		mnChangeTheme.add(mntmDarkMode);
		mnChangeTheme.add(mntmLightMode);
		mnPreference.add(mntmChangePort);
		mnPreference.add(mntmChangeIp);		
		mnUsername.add(mntmLogOut);		/* Adding menu items to menu*/
		mnUsername.add(mntmExit);
		menuBar.add(mnAdd);
		mnAdd.add(mntmAddFriend);
		menuBar.add(mnRequest);		
		mnRequest.add(mnSend);
		mnSend.add(listRequestSend);
		mnRequest.add(mnRecieved);
		mnRecieved.add(listRequestReceive);
		menuBar.setBounds(0,0,500,20);
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
		frame.getContentPane().setBackground(UI.getWallColor());
		menuBar.setForeground(UI.getTextColor());
		mnUsername.setForeground(UI.getTextColor());
		mnPreference.setForeground(UI.getTextColor());
		mnChangeTheme.setForeground(UI.getTextColor());
		mntmDarkMode.setForeground(UI.getTextColor());
		mntmLightMode.setForeground(UI.getTextColor());
		mntmChangePort.setForeground(UI.getTextColor());
		mntmChangeIp.setForeground(UI.getTextColor());
		mntmLogOut.setForeground(UI.getTextColor());
		mntmExit.setForeground(UI.getTextColor());
		mnAdd.setForeground(UI.getTextColor());
		mntmAddFriend.setForeground(UI.getTextColor());
		mnRequest.setForeground(UI.getTextColor());
		mnSend.setForeground(UI.getTextColor());
		listRequestReceive.setForeground(UI.getTextColor());
		mnRecieved.setForeground(UI.getTextColor());
		listRequestSend.setForeground(UI.getTextColor());

		mnUsername.setBackground(UI.getComponentColor());
		mnPreference.setBackground(UI.getComponentColor());
		mnChangeTheme.setBackground(UI.getComponentColor());
		mntmDarkMode.setBackground(UI.getComponentColor());
		mntmLightMode.setBackground(UI.getComponentColor());
		mntmChangeIp.setBackground(UI.getComponentColor());
		mntmChangePort.setBackground(UI.getComponentColor());
		mntmLogOut.setBackground(UI.getComponentColor());
		mntmExit.setBackground(UI.getComponentColor());
		mntmAddFriend.setBackground(UI.getComponentColor());
		mnSend.setBackground(UI.getComponentColor());
		mnRecieved.setBackground(UI.getComponentColor());
		listRequestSend.setBackground(UI.getComponentColor());
		listRequestReceive.setBackground(UI.getComponentColor());
	}
	/*
	 * For refreshing the menu bar items
	 */
	public void refreshMenuBarPanel(){
		try {
			setSendList();
			setReceivedList();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, "There's some error in getting data", "Oops !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	private void setSendList() throws SQLException{
		String query = "SELECT `user_information`.`username` FROM `user_information` INNER JOIN `invitation` ON `user_information`.id = `invitation`.to_id WHERE `invitation`.`from_id`=?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, id);
		resultSet = preparedStatement.executeQuery();
		sentListArray.removeAllElements();
		int i=0;
		while(resultSet.next()){
			sentListArray.addElement(resultSet.getString(1));
			i++;
		}		
		mnSend.setText("Send (" + i + ")");
	}
	private void setReceivedList() throws SQLException{
		String query = "SELECT `user_information`.`username` FROM `user_information` INNER JOIN `invitation` ON `user_information`.id = `invitation`.from_id WHERE `invitation`.`to_id`=?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, id);
		resultSet = preparedStatement.executeQuery();
		receviedListArray.removeAllElements();
		int i=0;
		while(resultSet.next()){
			receviedListArray.addElement(resultSet.getString(1));
			i++;
		}
		mnRecieved.setText("Received (" + i + ")");
	}
	/*
	 * Respond to send request
	 * 	for canceling sent request
	 */
	private void respondSendRequest() {
		String senderName = listRequestSend.getSelectedValue();
		int request = JOptionPane.showConfirmDialog(this, "Do you wanna cancel request to " + senderName);
		try {
			String toId = getIdFromUsername(senderName);
			if(request == 0) {
				if(deleteRequest(id,toId) != 0) {
					JOptionPane.showMessageDialog(this, "Request Cancelled", "Done !", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Can't respond to request", "Error !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * Respond to received request 
	 * @event when user click on user name from received request list
	 */
	private void respondReceivedInvite() {
		String[] options = {"Accept", "Reject"};
		String senderName = listRequestReceive.getSelectedValue();
		int request = JOptionPane.showOptionDialog(this, senderName + " wants to make a connection with you", "Invitation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		try {
			String fromId = getIdFromUsername(senderName);	
			
			if(request == 1) {	// Invitation rejected
				if(deleteRequest(fromId,id) != 0) {
					JOptionPane.showMessageDialog(this, "Request Cancelled", "Done !", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(this, "Request can't cancelled", "Error !", JOptionPane.INFORMATION_MESSAGE);
				}
			}			
			else if(request == 0) {	// Invitation accepted
				deleteRequest(fromId,id);
				acceptRequest(fromId,senderName);
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Can't respond to request", "Error !", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * delete request
	 * @call on accept
	 * @call reject
	 */
	private int deleteRequest(String fromId, String toId) throws SQLException{
		String query = "DELETE FROM `invitation` WHERE `to_id`=? AND `from_id`=?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, toId);
		preparedStatement.setString(2, fromId);
		return preparedStatement.executeUpdate();
	}
	/*
	 * acceptRequest
	 */
	private void acceptRequest(String fromId, String senderName) throws SQLException{
		String query = "INSERT INTO `connections`(`from_id`, `to_id`) VALUES (?,?),(?,?)";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, id);
		preparedStatement.setString(2, fromId);
		preparedStatement.setString(3, fromId);
		preparedStatement.setString(4, id);
		
		if(preparedStatement.executeUpdate() != 0) {
			JOptionPane.showMessageDialog(this, "You are now friend with " + senderName, "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/*
	 * Search friend and send request
	 * check for 	user is exist or not then
	 * check for 	already friend
	 * 				already request send
	 * 				already request come from searching user
	 */
	private void sendRequest() {
		String searchName = JOptionPane.showInputDialog("Enter username to search ");		
		if(searchName !=null){
			if(searchName.equals("") || searchName == null) {
				JOptionPane.showMessageDialog(this, "Enter name to search", "Empty Name", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(searchName.equals(userName)){
				JOptionPane.showMessageDialog(this, "You are already friend with yourself", "Invalid search", JOptionPane.INFORMATION_MESSAGE);
			}else {
				try {
					String toId = getIdFromUsername(searchName);
					if(toId != null) {
						if(checkRepeatRequest("connections", id,toId)) {
							if(checkRepeatRequest("invitation", toId, id)) {
								if(checkRepeatRequest("invitation", id, toId)) {
									String query = "INSERT INTO `invitation`(`from_id`, `to_id`) VALUES (?,?)";
									preparedStatement = connection.prepareStatement(query);
									preparedStatement.setString(1, id);
									preparedStatement.setString(2, toId);
									if(preparedStatement.executeUpdate() !=0){
										JOptionPane.showMessageDialog(this, "Request sent to " + searchName, "Invitation sent !", JOptionPane.DEFAULT_OPTION);
									}
								}
								else
									JOptionPane.showMessageDialog(this, "Request already sent to " + searchName, "Wait !", JOptionPane.DEFAULT_OPTION);
							} else
								JOptionPane.showMessageDialog(this, "You already have reequest from " + searchName, "Check Requests !", JOptionPane.DEFAULT_OPTION);
						} else {
							JOptionPane.showMessageDialog(this, "You already friend with " + searchName, "Already Friends", JOptionPane.DEFAULT_OPTION);
						}
					}
					else {
						JOptionPane.showMessageDialog(this, "No user found", "404", JOptionPane.INFORMATION_MESSAGE);
					}
				}catch(Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error in invitation", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
	/*
	 * to get id by using user name form `user_information` table
	 */
	private String getIdFromUsername(String username) throws SQLException{
		String from_idQuery = "SELECT `id` from `user_information` WHERE `username`=?";
		preparedStatement = connection.prepareStatement(from_idQuery);
		preparedStatement.setString(1, username);
		resultSet = preparedStatement.executeQuery();
		if(resultSet.next())
			return resultSet.getString(1);
		else 
			return null;
	}
	/*
	 * Check for already friend or request(received or sent)
	 */
	private boolean checkRepeatRequest(String table, String fromId, String toId) throws SQLException{
		String query = "SELECT `from_id`, `to_id` FROM `" + table + "` WHERE `from_id` = ? AND `to_id` = ?";
		System.out.println(query + " : " + fromId + " : " + toId);
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, fromId);
		preparedStatement.setString(2, toId);
		resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) 
			return false;
		else
			return true;
	}
}
