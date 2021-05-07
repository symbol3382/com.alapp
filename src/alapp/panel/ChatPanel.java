package alapp.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JTextArea;

import alapp.service.UserPortService;

import javax.swing.JScrollPane;

public class ChatPanel extends JPanel {

    JButton btnSend;

    static Socket s;
    int hostType;
    DataOutputStream dos;
    String ip;

    private JScrollPane scrollPaneSendMessage;
    private JTextArea taSend;
    private JScrollPane scrollPaneChatArea;
    private JTextArea taChatArea;

    UserPortService userPortService;

    public ChatPanel(int hostType, UserPortService userPortService) {
        this.hostType = hostType;
        this.userPortService = userPortService;
        setLayout(null);
        declareComponents();
    }

    private void declareComponents() {
        btnSend = new JButton("Send");
        scrollPaneSendMessage = new JScrollPane();
        taSend = new JTextArea();
        scrollPaneChatArea = new JScrollPane();
        taChatArea = new JTextArea();

        setBounds(0, 170, 484, 400);
        btnSend.setBounds(394, 321, 80, 68);
        scrollPaneSendMessage.setBounds(10, 321, 376, 69);
        scrollPaneChatArea.setBounds(10, 11, 464, 300);

        scrollPaneSendMessage.setViewportView(taSend);
        scrollPaneChatArea.setViewportView(taChatArea);

        add(btnSend);
        add(scrollPaneSendMessage);
        add(scrollPaneChatArea);

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    dos = new DataOutputStream(s.getOutputStream());
                    String m = taSend.getText().trim() + "\n";
                    taChatArea.setText(taChatArea.getText() + m);
                    dos.writeUTF(m);
                } catch (Exception e) {
                }
            }
        });
    }

    public void startChat() {
        ChatThread obj = new ChatThread(hostType, taChatArea, userPortService);
        obj.start();
    }
}
