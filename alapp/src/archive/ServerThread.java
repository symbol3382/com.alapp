package archive;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTable;
import javax.swing.JTextField;

public class ServerThread extends Thread{
	public static Socket s;
	public static ServerSocket ss;
	public static DataInputStream din;
	int port;
	public JTable j;
	
	 ServerThread(JTable j,int port){
		this.j=j;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			ss = new ServerSocket(port);
			Socket s = ss.accept();
			ChatAsServerPanel.s = s;
			din = new DataInputStream(s.getInputStream());
			String m = "";
			while(true) {
				m = din.readUTF();
				System.out.println("Server side(received); " + m);
				j.add(new JTextField(m,10));
			}
		}
		catch(Exception e) {
			
		}
	}
}
