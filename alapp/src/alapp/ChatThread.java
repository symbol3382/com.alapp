package alapp;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

public class ChatThread extends Thread{
	
	JTextArea ta;
	static Socket s;
	static ServerSocket ss;
	static DataInputStream din;
	
	int hostType;

	ChatThread(int hostType, JTextArea ta){
		this.ta = ta;
		this.hostType = hostType;
	}
	
	@Override
	public void run() {
		try {
			if(hostType == DBMethod.CLIENT_CHAT_PANEL) {
				s = new Socket(DBMethod.friendIP, DBMethod.friendPort);
			}
			else if(hostType == DBMethod.SERVER_CHAT_PANEL) {
				ss = new ServerSocket(Integer.parseInt(DBMethod.userPort));
				s = ss.accept();
			}
			ChatPanel.s = s;
			din = new DataInputStream(s.getInputStream());
			String m = "";
			while(true) {
				m = din.readUTF();
				ta.setText(ta.getText() + m);
			}
		}
		catch(Exception e) {
			
		}
	}
}
