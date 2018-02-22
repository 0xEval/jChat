/* ---------------------------------------------------------------------
 * ClientGUI.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 * --------------------------------------------------------------------- */

package UI;

import Socket.jClientSocket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class ClientGUI {

    private jClientSocket client;

    private JPanel      pnMainPanel;
    private JButton     btLogin;
    private JList       lsUserlist;
    private JTextArea   taChat;
    private JScrollPane scpChat;
    private JTextField  tfUsername;
    private JTextField  tfPassword;
    private JTextField  tfMessageInput;
    private JLabel      lbUsername;
    private JLabel      lbPassword;

    private final Color BG_COLOR   = new Color(30, 33, 37);   // Primary background color (main pane)
    private final Color BG2_COLOR  = new Color(52, 60, 65);   // Secondary " color (input fields)
    private final Color TEXT_COLOR  = new Color(148,155,162); // Text foreground color

    public ClientGUI(jClientSocket socket) {
        client = socket;
        initializeComponents();
        initializeListeners();
    }

    // Append a message with the given string in the Chat pane.
    public void updateChatWindow(String msg) {
        taChat.insert(msg + "\n", taChat.getText().length());
        taChat.setCaretPosition(taChat.getText().length());
    }

    // Update the user list with a given String array
    public void updateUserList(String [] list) {
        lsUserlist.setListData(list);
    }

    // Initialize listeners on every input fields
    public void initializeListeners() {
        tfMessageInput.addActionListener((ActionEvent lambda) -> {
            String input = tfMessageInput.getText();
            // A user can send a PM by appending a message with "/w" and a username
            if (input.startsWith("/w")) {
                String msg  = input.substring(input.indexOf(" ", input.indexOf(" ") + 1) + 1);
                String dest = input.split(" ")[1];
                client.sendMessage(msg, dest);
            } else {
                client.sendMessage(input);
            }
            tfMessageInput.setText("");
            tfMessageInput.requestFocusInWindow();
        });

        tfUsername.addActionListener((ActionEvent lambda) -> {
            String username = tfUsername.getText();
            client.login(username);
            // Hacky way to wait for server response
            try {
                Thread.sleep(100);
                if (client.isConnected()) {
                    tfUsername.setText(username);
                    tfUsername.setFont(new Font("Hack", Font.ITALIC + Font.BOLD, 14));
                    tfUsername.setEditable(false);
                    tfMessageInput.requestFocusInWindow();
                } else {
                    updateChatWindow("Username taken. Try again.");
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        });
    }

    // Initializes the GUI components and dispatch them on the panel.
    public void initializeComponents() {

        pnMainPanel = new JPanel();
        GridBagLayout gbMainPanel = new GridBagLayout();
        GridBagConstraints gbcMainPanel = new GridBagConstraints();
        pnMainPanel.setLayout( gbMainPanel );

        lbUsername = new JLabel("Username");
        lbUsername.setFont(new Font("Hack", Font.PLAIN, 14));
        lbUsername.setBackground(BG2_COLOR);
        lbUsername.setForeground(TEXT_COLOR);
        gbcMainPanel.gridx = 0;
        gbcMainPanel.gridy = 0;
        gbcMainPanel.gridwidth = 1;
        gbcMainPanel.gridheight = 2;
        gbcMainPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcMainPanel.weightx = 1;
        gbcMainPanel.weighty = 1;
        gbcMainPanel.anchor = GridBagConstraints.NORTHWEST;
        gbcMainPanel.insets = new Insets( 5,15,0,0 );
        gbMainPanel.setConstraints( lbUsername, gbcMainPanel );
        pnMainPanel.add( lbUsername );

        lbPassword = new JLabel("Password");
        lbPassword.setFont(new Font("Hack", Font.PLAIN, 14));
        lbPassword.setBackground(BG2_COLOR);
        lbPassword.setForeground(TEXT_COLOR);
        gbcMainPanel.gridx = 2;
        gbcMainPanel.gridy = 0;
        gbcMainPanel.gridwidth = 1;
        gbcMainPanel.gridheight = 2;
        gbcMainPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcMainPanel.weightx = 1;
        gbcMainPanel.weighty = 1;
        gbcMainPanel.anchor = GridBagConstraints.NORTHEAST;
        gbcMainPanel.insets = new Insets( 5,15,0,0 );
        gbMainPanel.setConstraints( lbPassword, gbcMainPanel );
        pnMainPanel.add( lbPassword );

        tfUsername = new JTextField( );
        tfUsername.setFont(new Font("Hack", Font.PLAIN, 14));
        tfUsername.setBackground(BG2_COLOR);
        tfUsername.setForeground(TEXT_COLOR);
        tfUsername.setCaretColor(TEXT_COLOR);
        tfUsername.setBorder(null);
        gbcMainPanel.gridx = 1;
        gbcMainPanel.gridy = 0;
        gbcMainPanel.gridwidth = 1;
        gbcMainPanel.gridheight = 1;
        gbcMainPanel.fill = GridBagConstraints.BOTH;
        gbcMainPanel.weightx = 6;
        gbcMainPanel.weighty = 0;
        gbcMainPanel.anchor = GridBagConstraints.NORTHEAST;
        gbcMainPanel.insets = new Insets( 5,0,5,0 );
        gbMainPanel.setConstraints( tfUsername, gbcMainPanel );
        pnMainPanel.add( tfUsername );

        tfPassword = new JPasswordField( );
        tfPassword.setBackground(BG2_COLOR);
        tfPassword.setForeground(TEXT_COLOR);
        tfPassword.setCaretColor(TEXT_COLOR);
        tfPassword.setBorder(null);
        gbcMainPanel.gridx = 3;
        gbcMainPanel.gridy = 0;
        gbcMainPanel.gridwidth = 1;
        gbcMainPanel.gridheight = 1;
        gbcMainPanel.fill = GridBagConstraints.BOTH;
        gbcMainPanel.weightx = 6;
        gbcMainPanel.weighty = 0;
        gbcMainPanel.anchor = GridBagConstraints.NORTHEAST;
        gbcMainPanel.insets = new Insets( 5,0,5,0 );
        gbMainPanel.setConstraints( tfPassword, gbcMainPanel );
        pnMainPanel.add( tfPassword );

        btLogin = new JButton( "Login"  );
        btLogin.setFont(new Font("Hack", Font.PLAIN, 14));
        btLogin.setBackground(BG2_COLOR);
        btLogin.setForeground(TEXT_COLOR);
        btLogin.setBorder(null);
        gbcMainPanel.gridx = 4;
        gbcMainPanel.gridy = 0;
        gbcMainPanel.gridwidth = 1;
        gbcMainPanel.gridheight = 1;
        gbcMainPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcMainPanel.weightx = 1;
        gbcMainPanel.weighty = 0;
        gbcMainPanel.anchor = GridBagConstraints.NORTH;
        gbcMainPanel.insets = new Insets( 5,5,5, 10 );
        gbMainPanel.setConstraints( btLogin, gbcMainPanel );
        pnMainPanel.add( btLogin );

        lsUserlist = new JList( );
        lsUserlist.setFont(new Font("Hack", Font.PLAIN, 14));
        lsUserlist.setBackground(BG2_COLOR);
        lsUserlist.setForeground(TEXT_COLOR);
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)lsUserlist.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scpUserlist = new JScrollPane( lsUserlist );
        scpUserlist.setBorder(null);
        gbcMainPanel.gridx = 4;
        gbcMainPanel.gridy = 1;
        gbcMainPanel.gridwidth = 1;
        gbcMainPanel.gridheight = 2;
        gbcMainPanel.fill = GridBagConstraints.BOTH;
        gbcMainPanel.weightx = 1;
        gbcMainPanel.weighty = 1;
        gbcMainPanel.anchor = GridBagConstraints.NORTH;
        gbcMainPanel.insets = new Insets( 0,5,10,10 );
        gbMainPanel.setConstraints( scpUserlist, gbcMainPanel );
        pnMainPanel.add( scpUserlist );

        taChat = new JTextArea(2, 10);
        taChat.setFont(new Font("Hack", Font.PLAIN, 14));
        taChat.setBackground(BG2_COLOR);
        taChat.setForeground(TEXT_COLOR);
        taChat.setEditable(false);
        scpChat = new JScrollPane( taChat );
        scpChat.setBorder(null);
        gbcMainPanel.gridx = 0;
        gbcMainPanel.gridy = 1;
        gbcMainPanel.gridwidth = 4;
        gbcMainPanel.gridheight = 1;
        gbcMainPanel.fill = GridBagConstraints.BOTH;
        gbcMainPanel.weightx = 1;
        gbcMainPanel.weighty = 1;
        gbcMainPanel.anchor = GridBagConstraints.NORTH;
        gbcMainPanel.insets = new Insets( 0,10,0,0 );
        gbMainPanel.setConstraints( scpChat, gbcMainPanel );
        pnMainPanel.add( scpChat );

        tfMessageInput = new JTextField( );
        tfMessageInput.setFont(new Font("Hack", Font.PLAIN, 14));
        tfMessageInput.setBackground(BG2_COLOR);
        tfMessageInput.setForeground(TEXT_COLOR);
        tfMessageInput.setCaretColor(TEXT_COLOR);
        tfMessageInput.setBorder(null);
        gbcMainPanel.gridx = 0;
        gbcMainPanel.gridy = 2;
        gbcMainPanel.gridwidth = 4;
        gbcMainPanel.gridheight = 1;
        gbcMainPanel.fill = GridBagConstraints.BOTH;
        gbcMainPanel.weightx = 1;
        gbcMainPanel.weighty = 0;
        gbcMainPanel.anchor = GridBagConstraints.NORTH;
        gbcMainPanel.insets = new Insets( 10,10,10,0 );
        gbMainPanel.setConstraints( tfMessageInput, gbcMainPanel );
        pnMainPanel.add( tfMessageInput );

        pnMainPanel.setVisible(true);
        pnMainPanel.setBackground(BG_COLOR);
        JFrame chatFrame = new JFrame("jChat v0.1");
        chatFrame.add(pnMainPanel);
        chatFrame.setSize(900, 500);
        chatFrame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Centers the window in the middle of the screen on runtime
        chatFrame.setLocation(
                dim.width/2-chatFrame.getSize().width/2,
                dim.height/2-chatFrame.getSize().height/2
        );
        tfMessageInput.requestFocusInWindow();
    }

}
