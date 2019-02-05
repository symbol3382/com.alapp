package alapp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserLogin {
	private JFrame frame;
	/* 
	 * Current friend chat details 
	 * 
	 * Panels
	 */
	private MenuBarPanel menuBarPanel;
	private SelectChatPanel selectChatPanel;
	private FriendDetailPanel friendDetailPanel;
	/*
	 * Connection details
	 */
	public UserLogin() {
		initialize();
	}
	private void initialize() {
		declareComponents();
		setBounds();
		setBackground();
		setColor();
		DBMethod.initializePortNumber(frame);
		DBMethod.setStatusActive(frame);
		/*
		 * Adding panel
		 */
		frame.getContentPane().add(menuBarPanel);
		frame.getContentPane().add(selectChatPanel);
		frame.getContentPane().add(friendDetailPanel);
	
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
		
		friendDetailPanel = new FriendDetailPanel(frame);
		selectChatPanel = new SelectChatPanel(friendDetailPanel);
		friendDetailPanel.setSelectChatPanel(selectChatPanel);
		menuBarPanel = new MenuBarPanel(DBMethod.userId,DBMethod.userName,frame,selectChatPanel);
	}
	private void setBounds() {
		frame.setBounds(0, 0, 500, 209);
		menuBarPanel.setBounds(0,0,484,20);
		selectChatPanel.setBounds(0,20,484,30);
		friendDetailPanel.setBounds(0,50,484,120);
	}	
	private void setBackground() {
		frame.getContentPane().setBackground(UI.getWallColor());
	}
	private void setColor() {
		frame.setBackground(UI.getWallColor());
	}
	public void setVisible(boolean state, JFrame parentFrame) {
		frame.setVisible(true);
		frame.setLocationRelativeTo(parentFrame);
	}
	public void exit() {
		try {
			DBMethod.freeIPPort(frame);
			DBMethod.exit(frame);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error in setting status offline", "Can't get offline", JOptionPane.ERROR_MESSAGE);
		}
	}
}