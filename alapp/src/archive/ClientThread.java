package archive;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ClientThread extends Thread{
	JTable j;
	public static Socket s;
	public static ServerSocket ss;
	public static DataInputStream din;
	public JTextField tf;
	int port;
	String ip;
	ClientThread(JTable j,String ip,int port){
		this.j=j;
		this.port = port;
		this.ip = ip;
	}
	
	@Override
	public void run() {
		try {
			s = new Socket(ip, port);
			din = new DataInputStream(s.getInputStream());
			String m = "";
			while(true) {
				m = din.readUTF();
				System.out.println("Client side(received); " + m);
				j.add(new JTextField(m,10));
			}
		}
		catch(Exception e) {
			
		}
	}
}
