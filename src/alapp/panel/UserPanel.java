package alapp.panel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alapp.Main;
import alapp.config.UIColorConfig;
import alapp.model.User;
import alapp.service.UserPortService;
import alapp.service.UserService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserPanel {
	private JFrame frame;
	/*
	 * Current friend chat details
	 * 
	 * Panels
	 */
	private UserMenuBarPanel menuBarPanel;
	private UserChatSelectPanel userChatSelectPanel;
	private UserFriendDetailPanel userFriendDetailPanel;

	private User user;
	private UserService userService;
	private UserPortService userPortService;

	/*
	 * Connection details
	 */
	public UserPanel(UserService userService) {
		this.user = userService.getUser();
		this.userService = userService;
		initialize();
	}

	private void initialize() {
		userPortService = new UserPortService(user.getId(), frame);
		userPortService.initializePortNumber();
		declareComponents();
		setBounds();
		setBackground();
		setColor();

		userService.setStatusActive(frame);


		/*
		 * Adding panel
		 */
		frame.getContentPane().add(menuBarPanel);
		frame.getContentPane().add(userChatSelectPanel);
		frame.getContentPane().add(userFriendDetailPanel);

		/*
		 * chatSelectPanel listener
		 */
		userChatSelectPanel.btnReferesh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userChatSelectPanel.refreshFriendList();
			}
		});
		userChatSelectPanel.cbFriendList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				userFriendDetailPanel.refereshFriendPanel();
			}
		});
		
		menuBarPanel.mntmLogOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
				new Main(frame);
				frame.setVisible(false);
			}
		});
		/*
		 * exit
		 */
		menuBarPanel.mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				exit();
				System.exit(0);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exit();
			}
		});
	}

	/*
	 * Initializing components and panel
	 */
	private void declareComponents() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		userChatSelectPanel = new UserChatSelectPanel(frame, user);
		userFriendDetailPanel = new UserFriendDetailPanel(frame, user, userPortService, userChatSelectPanel);
		menuBarPanel = new UserMenuBarPanel(frame, userService, userChatSelectPanel);
		
		userChatSelectPanel.setUserFriendDetailPanel(userFriendDetailPanel);
		userChatSelectPanel.refreshFriendList();
	}

	private void setBounds() {
		frame.setBounds(0, 0, 500, 209);
		menuBarPanel.setBounds(0, 0, 484, 20);
		userChatSelectPanel.setBounds(0, 20, 484, 30);
		userFriendDetailPanel.setBounds(0, 50, 484, 120);
	}

	private void setBackground() {
		frame.getContentPane().setBackground(UIColorConfig.getWallColor());
	}

	private void setColor() {
		frame.setBackground(UIColorConfig.getWallColor());
	}

	public void setVisible(boolean state, JFrame parentFrame) {
		frame.setVisible(true);
		frame.setLocationRelativeTo(parentFrame);
	}

	public void exit() {
		try {
			userPortService.freeIPPort(frame);
			userService.exit(frame);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error in setting status offline", "Can't get offline",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}