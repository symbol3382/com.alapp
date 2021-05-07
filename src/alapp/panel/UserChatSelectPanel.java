package alapp.panel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import alapp.config.UIColorConfig;
import alapp.model.User;
import alapp.service.FriendService;
import java.awt.Font;
import javax.swing.JButton;

public class UserChatSelectPanel extends JPanel {

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    String selectedFriendId;

    JLabel lblChatWith;
    DefaultComboBoxModel<User> dcbmFriendsList;
    JComboBox<User> cbFriendList;
    JButton btnReferesh;
    UserFriendDetailPanel userFriendDetailPanel;

    FriendService friendService;
    User user;
    JFrame frame;

    public DefaultComboBoxModel<User> getDcbmFriendsList() {

        return dcbmFriendsList;
    }

    public void setUserFriendDetailPanel(UserFriendDetailPanel userFriendDetailPanel) {
        this.userFriendDetailPanel = userFriendDetailPanel;
    }

    public UserChatSelectPanel(JFrame frame, User user) {
        this.user = user;
        this.frame = frame;
        friendService = new FriendService(user, frame);

        setLayout(null);
        declareComponents();
        setBackgroundColor();
        setColor();
    }

    void declareComponents() {
        lblChatWith = new JLabel("Chat With");
        lblChatWith.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
        dcbmFriendsList = new DefaultComboBoxModel<User>();
        cbFriendList = new JComboBox<User>(dcbmFriendsList);
        Icon icon = new ImageIcon("assets/reload_icon_light_mode.png");
        btnReferesh = new JButton("", icon);
        lblChatWith.setBounds(10, 8, 50, 14);
        cbFriendList.setBounds(70, 4, 372, 22);
        btnReferesh.setBounds(449, 3, 24, 24);

        add(lblChatWith);
        add(cbFriendList);
        add(btnReferesh);
    }

    /*
	 * set background to null
     */
    private void setBackgroundColor() {
        setBackground(null);
        btnReferesh.setBackground(null);
        btnReferesh.setBorder(null);
    }

    /*
	 * set color for UI Design
     */
    void setColor() {
        lblChatWith.setForeground(UIColorConfig.getTextColor());
        cbFriendList.setBackground(UIColorConfig.getComponentColor());
        cbFriendList.setForeground(UIColorConfig.getTextColor());
    }

    /*
	 * To get friends list from connections
     */
    void refreshFriendList() {
        dcbmFriendsList = friendService.listFriends(frame);
        cbFriendList.setModel(dcbmFriendsList);
        userFriendDetailPanel.refereshFriendPanel();
    }

    /*
	 * to get friend list for friendPanel 
	 * show no value when friendListcount is zero
     */
    public User getSelectedFriend() {
        if (dcbmFriendsList.getSize() > 0) {
            return dcbmFriendsList.getElementAt(cbFriendList.getSelectedIndex());
        }
        return null;
    }
}
