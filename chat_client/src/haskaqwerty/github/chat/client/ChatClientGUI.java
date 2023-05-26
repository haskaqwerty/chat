package haskaqwerty.github.chat.client;

import haskaqwerty.github.chat.server.library.DefaultGUIExceptionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClientGUI extends JFrame implements ActionListener {
//    public static final int POS_X = 600;
//    public static final int POS_Y = 150;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 300;
    public static final String TITLE = "Chat Client";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatClientGUI();
            }
        });
    }

    private final JPanel upperPanel = new JPanel(new GridLayout(2,3));
    private final JTextField fieldIPAddr = new JTextField("82.222.249.131");
    private final JTextField fieldPort = new JTextField("8189");
    private final JCheckBox chkAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField fieldLogin = new JTextField("login_1");
    private final JPasswordField fieldPass = new JPasswordField("pass_1");
    private final JButton btnLogin = new JButton("LOGIN");
    private final JTextArea log = new JTextArea();
    private final JList<String> userList = new JList<>();
    private final JPanel bottomPanel = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("DISCONNECT");
    private final JTextField fieldInput = new JTextField();
    private final JButton btnSend = new JButton("SEND");

    private ChatClientGUI()
    {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultGUIExceptionHandler());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(TITLE);
        setLocationRelativeTo(null);
        setSize(WIDTH,HEIGHT);

        btnLogin.addActionListener(this);
        btnDisconnect.addActionListener(this);
        btnSend.addActionListener(this);
        fieldIPAddr.addActionListener(this);
        fieldPort.addActionListener(this);
        fieldPass.addActionListener(this);
        fieldLogin.addActionListener(this);
        fieldInput.addActionListener(this);
        chkAlwaysOnTop.addActionListener(this);


        upperPanel.add(fieldIPAddr);
        upperPanel.add(fieldPort);
        upperPanel.add(chkAlwaysOnTop);
        upperPanel.add(fieldLogin);
        upperPanel.add(fieldPass);
        upperPanel.add(btnLogin);
        add(upperPanel, BorderLayout.NORTH);

        JScrollPane scrollLog = new JScrollPane(log);
        log.setEditable(false);
        add(scrollLog,BorderLayout.CENTER);
        JScrollPane scrollUsers = new JScrollPane(userList);
        scrollUsers.setPreferredSize(new Dimension(150,0));
        add(scrollUsers,BorderLayout.EAST);


        bottomPanel.add(btnDisconnect,BorderLayout.WEST);
        bottomPanel.add(fieldInput,BorderLayout.CENTER);
        bottomPanel.add(btnSend,BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setVisible(false);


        setAlwaysOnTop(chkAlwaysOnTop.isSelected());
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object src =e.getSource();

        if (src == fieldIPAddr || src == fieldPort || src == fieldPass || src ==fieldLogin || src == btnLogin) {
            connect();
        } else
        if (src == btnLogin) {

        } else if (src == btnDisconnect) {
            disconnect();

        } else if (src == btnSend || src == fieldInput) {
            sendMsg();
        } else
            if (src ==chkAlwaysOnTop) {
                setAlwaysOnTop(chkAlwaysOnTop.isSelected());
            }
        else {
            throw new RuntimeException("Unknown src = " + src);
        }
    }
    private void connect()
    {
        upperPanel.setVisible(false);
        bottomPanel.setVisible(true);
    }
    private void disconnect()
    {
        bottomPanel.setVisible(false);
        upperPanel.setVisible(true);
    }
    private void sendMsg()
    {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        fieldInput.requestFocus();
        log.append(msg+"\n");
    }

}
