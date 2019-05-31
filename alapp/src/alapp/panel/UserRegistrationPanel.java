package alapp.panel;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import alapp.config.UIColorConfig;
import alapp.model.User;
import alapp.service.UserService;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.event.ActionEvent;

public class UserRegistrationPanel {

	JFrame frame;
	JFrame activityFrame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JLabel label;
	private JLabel lblRegistration;
	private JLabel lblNewLabel;
	private JLabel lblLastName;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblRetypePassword;
	private JButton btnSignUp = new JButton("Sign Up");
	private JButton btnColorMode = new JButton("Dark Mode");

	UserService userService;

	public UserRegistrationPanel(JFrame activityFrame) {
		this.activityFrame = activityFrame;
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 390);
		frame.getContentPane().setLayout(null);
		userService = new UserService();
		initialize();

		// Event Handling
		btnColorMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnColorMode.setText(UIColorConfig.changeButton());
				setColor();
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				activityFrame.setVisible(true);
				frame.setVisible(false);
			}
		});

		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				signup();

			}
		});

	}

	private void initialize() {
		label = new JLabel("alapp");
		lblRegistration = new JLabel("Registration");
		lblNewLabel = new JLabel("First Name");
		lblLastName = new JLabel("Last Name");
		lblUsername = new JLabel("Username");
		lblPassword = new JLabel("Password");
		lblRetypePassword = new JLabel("Retype Password");
		textField = new JTextField();
		textField_1 = new JTextField();
		textField_2 = new JTextField();
		passwordField = new JPasswordField();
		passwordField_1 = new JPasswordField();
		btnSignUp = new JButton("Sign Up");
		btnColorMode = new JButton();
		btnColorMode.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));

		label.setFont(new Font("Segoe UI Semilight", Font.BOLD, 20));
		lblRegistration.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
		lblNewLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
		lblLastName.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
		lblUsername.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
		lblPassword.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
		lblRetypePassword.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
		textField.setColumns(10);
		textField_1.setColumns(10);
		textField_2.setColumns(10);

		label.setForeground(new Color(40, 180, 99));
		textField.setBackground(null);
		textField_1.setBackground(null);
		textField_2.setBackground(null);
		passwordField.setBackground(null);
		passwordField_1.setBackground(null);
		btnSignUp.setBackground(null);
		btnColorMode.setBackground(null);

		setColor();

		label.setBounds(10, 11, 52, 27);
		lblRegistration.setBounds(199, 60, 102, 27);
		lblNewLabel.setBounds(91, 112, 57, 14);
		lblLastName.setBounds(91, 147, 57, 14);
		lblUsername.setBounds(91, 182, 57, 14);
		lblPassword.setBounds(91, 218, 57, 14);
		lblRetypePassword.setBounds(59, 253, 89, 14);
		textField.setBounds(183, 110, 208, 20);
		textField_1.setBounds(183, 145, 208, 20);
		textField_2.setBounds(183, 180, 208, 20);
		passwordField.setBounds(183, 215, 208, 20);
		passwordField_1.setBounds(183, 250, 208, 20);
		btnSignUp.setBounds(210, 300, 80, 20);
		btnColorMode.setBounds(10, 317, 100, 20);

		frame.getContentPane().add(label);
		frame.getContentPane().add(lblRegistration);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(lblLastName);
		frame.getContentPane().add(lblUsername);
		frame.getContentPane().add(lblPassword);
		frame.getContentPane().add(lblRetypePassword);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(textField_1);
		frame.getContentPane().add(textField_2);
		frame.getContentPane().add(passwordField);
		frame.getContentPane().add(passwordField_1);
		frame.getContentPane().add(btnSignUp);
		frame.getContentPane().add(btnColorMode);
	}

	private void setColor() {
		frame.getContentPane().setBackground(UIColorConfig.getWallColor());
		lblRegistration.setForeground(UIColorConfig.getTextColor());
		lblNewLabel.setForeground(UIColorConfig.getTextColor());
		lblLastName.setForeground(UIColorConfig.getTextColor());
		lblUsername.setForeground(UIColorConfig.getTextColor());
		lblPassword.setForeground(UIColorConfig.getTextColor());
		lblRetypePassword.setForeground(UIColorConfig.getTextColor());

		textField.setForeground(UIColorConfig.getTextColor());
		textField_1.setForeground(UIColorConfig.getTextColor());
		textField_2.setForeground(UIColorConfig.getTextColor());
		passwordField.setForeground(UIColorConfig.getTextColor());
		passwordField_1.setForeground(UIColorConfig.getTextColor());
		btnSignUp.setForeground(UIColorConfig.getTextColor());
		btnColorMode.setForeground(UIColorConfig.getTextColor());

		textField.setCaretColor(UIColorConfig.getTextColor());
		textField_1.setCaretColor(UIColorConfig.getTextColor());
		textField_2.setCaretColor(UIColorConfig.getTextColor());
		passwordField.setCaretColor(UIColorConfig.getTextColor());
		passwordField_1.setCaretColor(UIColorConfig.getTextColor());

		btnColorMode.setText(UIColorConfig.getSetMode());
	}

	private void signup() {
		if (userService.isUserExist(frame, textField_2.getText()) == null) {// User not Exist
			if (String.valueOf(passwordField.getPassword()).equals(String.valueOf(passwordField_1.getPassword()))) { // Password
																														// Match
				if (userService.addUser(new User(null, textField.getText(), textField_1.getText(),
						textField_2.getText(), String.valueOf(passwordField.getPassword()), null, null))) {
					JOptionPane.showMessageDialog(frame, "Done !", "Registration Successfull",
							JOptionPane.INFORMATION_MESSAGE);
				} else { // Registration failed
					JOptionPane.showMessageDialog(frame, "Error", "There's a some problem in creating your account",
							JOptionPane.ERROR_MESSAGE);
				}
			} else { // Password did not match
				JOptionPane.showMessageDialog(frame, "Error", "Password did not match", JOptionPane.ERROR_MESSAGE);
			}
		} else { // User exist
			JOptionPane.showMessageDialog(frame, "Username already taken", "Use different Username",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}