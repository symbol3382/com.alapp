package archive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JTable;

public class ChatAsServerPanel extends JPanel {
	JTextField textField;
	JButton btnSend;
	DataOutputStream dos;
	
	static Socket s;
	int port;
	
	private JTable table;
	/**
	 * Create the panel.
	 */
	public ChatAsServerPanel(int port) {
		this.port = port;
		setLayout(null);
		declareComponents();
		System.out.println("Making server on : " + port);
	}
	private void declareComponents() {
		textField = new JTextField();
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Server side(sending) " + textField.getText());
				try{
					dos = new DataOutputStream(s.getOutputStream());
					String m = textField.getText().trim() + "\n";
					dos.writeUTF(m);
				}
				catch(Exception e) {
					
				}
			}
		});
		btnSend.setBounds(393, 327, 81, 62);
		textField.setBounds(10, 329, 373, 60);
		textField.setColumns(10);
		add(textField);
		add(btnSend);
		
		table = new JTable();
		table.setBounds(10, 11, 464, 305);
		add(table);
	}
	void startChat() {
		ServerThread obj = new ServerThread(table, port);
		obj.start();
	}
}
