package alapp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;

public class AdminPane {

	private JFrame frame;
	private String id;
	public AdminPane(String id) {
		this.id = id;
		initialize();
	}

	public void setVisible(boolean set) {
		frame.setVisible(set);
	}
	private void initialize() {
		frame = new JFrame();
		JButton btnChangeServerIp = new JButton("Change Server IP");
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(2,2));
		frame.setLocationRelativeTo(frame);
		frame.getContentPane().add(btnChangeServerIp);
		
		btnChangeServerIp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateIpNumber();
			}
		});
	}
	private void updateIpNumber() {
		String ip = JOptionPane.showInputDialog(null, "Enter Ip", "IP");
		String query = "UPDATE `user_port` SET `ip`=? WHERE `id` = ?";
		Connection connection = DBInfo.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, ip);
			preparedStatement.setString(2, id);
			
			if(preparedStatement.executeUpdate() !=0) {
				JOptionPane.showMessageDialog(null, "Successfully updated");
			}
			else {
				JOptionPane.showMessageDialog(null, "Can't set ip address");
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Can't set ip address");
		}
	}
}