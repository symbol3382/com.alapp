package alapp.panel;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

import alapp.service.UserPortService;

public class ChatThread extends Thread{
	
	JTextArea ta;
	static Socket s;
	static ServerSocket ss;
	static DataInputStream din;
	
	int hostType;
	UserPortService userPortService;
	
	final int SERVER_CHAT_PANEL = 1;
	final int CLIENT_CHAT_PANEL = 2;

	ChatThread(int hostType, JTextArea ta, UserPortService userPortService){
		this.ta = ta;
		this.hostType = hostType;
		this.userPortService = userPortService;
	}
	
	@Override
	public void run() {
		try {
			if(hostType == CLIENT_CHAT_PANEL) {
				s = new Socket(userPortService.getUserPort().getIp(), Integer.parseInt(userPortService.getUserPort().getPort()));
			}
			else if(hostType == SERVER_CHAT_PANEL) {
				ss = new ServerSocket(Integer.parseInt(userPortService.getUserPort().getPort()));
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
