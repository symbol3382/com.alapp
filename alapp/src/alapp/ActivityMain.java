package alapp;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class ActivityMain {

	static JFrame frame;
	private JTextField tfUsername;
	private JPasswordField pfPassword;
	private JLabel lblAppName;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JButton btnLogin;
	private JButton btnCreateAnAccount;
	private JButton btnForgotPassword;
	private JButton btnColorMode;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ActivityMain();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ActivityMain() {
		initialize();
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

	private void initialize() {
		declareComponent();
		setFont();
		setBounds();
		UI.lightTheme();
		setColor();
		setBackground();
		addComponent();
		
		/* Event Handling */

		btnLogin.addActionListener(new ActionListener() { // Login button
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String userName = tfUsername.getText();
				String userPass = String.valueOf(pfPassword.getPassword());

				Connection connection = DBInfo.getConnection();
				String query = "SELECT `id`,`user_type` FROM `user_information` WHERE `username` = ? AND `password` = ?";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, userName);
					preparedStatement.setString(2, userPass);

					ResultSet resultSet = preparedStatement.executeQuery();
					if (resultSet.next()) {
						if (resultSet.getString(2).equals("U")) {
							DBMethod.userName = userName;
							DBMethod.userId = resultSet.getString(1);
							UserLogin userLogin = new UserLogin();
							userLogin.setVisible(true, frame);
							frame.setVisible(false);
						} else {
							AdminPane adminPane = new AdminPane(resultSet.getString(1));
							adminPane.setVisible(true);
							frame.setVisible(false);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Invalid Details !", "Username password did not match",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnCreateAnAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UserRegistration userRegistration = new UserRegistration();
				userRegistration.frame.setLocationRelativeTo(frame);
				userRegistration.frame.setVisible(true);
				frame.setVisible(false);
			}
		});

		btnColorMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnColorMode.setText(UI.changeButton());
				setColor();
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				setColor();
			}
		});

		ActivityMain.frame.setLocationRelativeTo(ActivityMain.frame);
		ActivityMain.frame.setVisible(true);

	}

	public void setColor() {
		frame.getContentPane().setBackground(UI.getWallColor());

		lblUsername.setForeground(UI.getTextColor());
		lblPassword.setForeground(UI.getTextColor());

		tfUsername.setForeground(UI.getTextColor());
		tfUsername.setCaretColor(UI.getTextColor());
		pfPassword.setForeground(UI.getTextColor());
		pfPassword.setCaretColor(UI.getTextColor());

		btnLogin.setForeground(UI.getTextColor());

		btnCreateAnAccount.setForeground(UI.getTextColor());
		btnForgotPassword.setForeground(UI.getTextColor());

		btnColorMode.setForeground(UI.getTextColor());
		lblAppName.setForeground(UI.getThemeColor());
	}
}
