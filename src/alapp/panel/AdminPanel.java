package alapp.panel;

import javax.swing.JFrame;

import alapp.model.User;

import java.awt.Frame;
import java.awt.GridLayout;

public class AdminPanel {

    private User admin;
    private JFrame frame;

    public AdminPanel(User admin) {
        this.admin = admin;
        initialize();
    }

    public void setVisible(boolean set, Frame frame) {
        frame.setVisible(set);
        frame.setLocationRelativeTo(frame);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(2, 2));
        frame.setLocationRelativeTo(frame);
    }

//	 To be use 
//	private void updateIpNumber() {
//		String ip = JOptionPane.showInputDialog(null, "Enter Ip", "IP");
//		String query = "UPDATE `user_port` SET `ip`=? WHERE `id` = ?";
//		Connection connection = DatabaseConfig.getConnection();
//		try {
//			PreparedStatement preparedStatement = connection.prepareStatement(query);
//			preparedStatement.setString(1, ip);
//			preparedStatement.setString(2, admin.getId());
//			
//			if(preparedStatement.executeUpdate() !=0) {
//				JOptionPane.showMessageDialog(null, "Successfully updated");
//			}
//			else {
//				JOptionPane.showMessageDialog(null, "Can't set ip address");
//			}
//		} catch(Exception e) {
//			JOptionPane.showMessageDialog(null, "Can't set ip address");
//		}
//	}
}
