package alapp;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import alapp.config.UIColorConfig;
import alapp.panel.AdminPanel;
import alapp.panel.UserPanel;
import alapp.panel.UserRegistrationPanel;
import alapp.service.UserService;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Main {

	private JFrame frame;
	private JTextField tfUsername;
	private JPasswordField pfPassword;
	private JLabel lblAppName;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JButton btnLogin;
	private JButton btnCreateAnAccount;
	private JButton btnForgotPassword;
	private JButton btnColorMode;

	UserService userService;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		initialize();
	}

	/* 
	 * to be called after logout
	 */
	public Main(JFrame frame) {
		this.frame = frame;
		initialize();
	}
	
	private void initialize() {
		declareComponent();
		setFont();
		setBounds();
		UIColorConfig.lightTheme();
		setColor();
		setBackground();
		addComponent();

		frame.setLocationRelativeTo(frame);
		frame.setVisible(true);

		/* Event Handling */

		btnLogin.addActionListener(new ActionListener() { // Login button

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String username = tfUsername.getText();
				String userPassword = String.valueOf(pfPassword.getPassword());

				userService = new UserService();
				String userTypeForVerification = userService.checkUserForLogin(username, userPassword);

				if (userTypeForVerification.equals("U")) {
					UserPanel userPanel = new UserPanel(userService);
					userPanel.setVisible(true, frame);
					frame.setVisible(false);
				} else if (userTypeForVerification.equals("A")) {
					AdminPanel adminPane = new AdminPanel(userService.getUser());
					adminPane.setVisible(true, frame);
					frame.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(frame, "Invalid Details !", "Username password did not match",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnCreateAnAccount.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				new UserRegistrationPanel(frame);
			}
		});

		btnColorMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnColorMode.setText(UIColorConfig.changeButton());
				setColor();
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				setColor();
			}
		});

	}

	private void declareComponent() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lblAppName = new JLabel("alapp");
		lblUsername = new JLabel("Username");
		lblPassword = new JLabel("Password");
		btnLogin = new JButton("Login");
		btnColorMode = new JButton("Dark Mode");

		pfPassword = new JPasswordField(10);
		tfUsername = new JTextField(10);

		btnCreateAnAccount = new JButton("Create an account");
		btnForgotPassword = new JButton("Forgot Password?");
	}

	private void setFont() {
		Font font = new Font("Segoe UI Semibold", Font.PLAIN, 12);
		pfPassword.setFont(font.deriveFont(Font.PLAIN, 12));
		tfUsername.setFont(font.deriveFont(Font.PLAIN, 12));
		lblUsername.setFont(font.deriveFont(Font.BOLD, 14));
		lblPassword.setFont(font.deriveFont(Font.BOLD, 14));
		lblAppName.setFont(font.deriveFont(Font.BOLD, 20));
		btnColorMode.setFont(font.deriveFont(Font.PLAIN, 11));
		btnCreateAnAccount.setFont(font.deriveFont(Font.PLAIN, 11));
		btnForgotPassword.setFont(font.deriveFont(Font.PLAIN, 11));
	}

	private void setBounds() {
		frame.setBounds(100, 100, 450, 300);
		tfUsername.setBounds(171, 71, 173, 20);
		pfPassword.setBounds(171, 99, 173, 20);
		lblAppName.setBounds(200, 25, 52, 27);
		lblUsername.setBounds(99, 72, 62, 14);
		lblPassword.setBounds(102, 100, 59, 14);
		btnLogin.setBounds(190, 130, 70, 23);
		btnColorMode.setBounds(10, 217, 100, 20);
		btnCreateAnAccount.setBounds(324, 192, 100, 23);
		btnForgotPassword.setBounds(324, 216, 100, 23);
	}

	private void addComponent() {
		frame.getContentPane().add(tfUsername);
		frame.getContentPane().add(pfPassword);
		frame.getContentPane().add(lblAppName);
		frame.getContentPane().add(lblUsername);
		frame.getContentPane().add(lblPassword);
		frame.getContentPane().add(btnLogin);
		frame.getContentPane().add(btnColorMode);
		frame.getContentPane().add(btnCreateAnAccount);
		frame.getContentPane().add(btnForgotPassword);
	}

	private void setBackground() {
		btnForgotPassword.setBorder(null);
		btnCreateAnAccount.setBorder(null);
		tfUsername.setBackground(null);
		pfPassword.setBackground(null);
		btnLogin.setBackground(null);
		btnCreateAnAccount.setBackground(null);
		btnForgotPassword.setBackground(null);
		btnColorMode.setBackground(null);
	}

	public void setColor() {
		frame.getContentPane().setBackground(UIColorConfig.getWallColor());

		lblUsername.setForeground(UIColorConfig.getTextColor());
		lblPassword.setForeground(UIColorConfig.getTextColor());

		tfUsername.setForeground(UIColorConfig.getTextColor());
		tfUsername.setCaretColor(UIColorConfig.getTextColor());
		pfPassword.setForeground(UIColorConfig.getTextColor());
		pfPassword.setCaretColor(UIColorConfig.getTextColor());

		btnLogin.setForeground(UIColorConfig.getTextColor());

		btnCreateAnAccount.setForeground(UIColorConfig.getTextColor());
		btnForgotPassword.setForeground(UIColorConfig.getTextColor());

		btnColorMode.setForeground(UIColorConfig.getTextColor());
		lblAppName.setForeground(UIColorConfig.getThemeColor());
	}
}
