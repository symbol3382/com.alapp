package archive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JTable;

public class ChatAsClientPanel extends JPanel {
	JTextField textField;
	JButton btnSend;
	DataOutputStream dos;
	String ip;	
	static Socket s;
	int port;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public ChatAsClientPanel(String ip,int port) {
		setLayout(null);
		this.ip = ip;
		this.port = port;
		declareComponents();
		System.out.println("Connecting(as client) with : " + port + " on ip : " + ip);
	}
	private void declareComponents() {
		textField = new JTextField();
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Client : sending message : " + textField.getText());
				try{
					dos = new DataOutputStream(s.getOutputStream());
					String m = textField.getText().trim() + "\n";
					System.out.println("Client Side(Sending) : " + m);
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
		table.setBounds(10, 11, 458, 306);
		add(table);
	}
	public void startClient() {
		ClientThread obj = new ClientThread(table, ip, port);
		obj.start();
	}
}
