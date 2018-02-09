package UI;

import java.awt.*;
import javax.swing.*;

public class jClientGUI {

    private JPanel      pnMainPanel;
    private JButton     btLogin;
    private JList       lsUserlist;
    private JTextArea   taChat;
    private JTextField  tfUsername;
    private JTextField  tfPassword;
    private JTextField  tfMessageInput;
    private JLabel      lbUsername;
    private JLabel      lbPassword;

    private final Color BG_COLOR   = new Color(30, 33, 37);
    private final Color BG2_COLOR  = new Color(52, 60, 65);
    private final Color TEXT_COLOR = new Color(148,155,162);

    public jClientGUI() {

        initializeComponents();

    }

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

        String []dataUserlist = { "Chocolate", "Ice Cream", "Apple Pie" };
        lsUserlist = new JList( dataUserlist );
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

        taChat = new JTextArea(2,10);
        taChat.setFont(new Font("Hack", Font.PLAIN, 14));
        taChat.setBackground(BG2_COLOR);
        taChat.setForeground(TEXT_COLOR);
        taChat.setEditable(false);
        JScrollPane scpChat = new JScrollPane( taChat );
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
        chatFrame.setSize(800, 500);
        chatFrame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        chatFrame.setLocation(
                dim.width/2-chatFrame.getSize().width/2,
                dim.height/2-chatFrame.getSize().height/2
        );

    }

    public static void main(String[] args) {

        jClientGUI GUI = new jClientGUI();

    }

}
