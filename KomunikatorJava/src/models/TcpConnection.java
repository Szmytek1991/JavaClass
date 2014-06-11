package models;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import windows.MainWindow;

public class TcpConnection {
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;
    private static JTextArea textArea = null;
    private static JScrollPane scrollPane = null;
    private static boolean connected = false;
    private static Socket s = null;

    public static void start(String username, String ip) throws Exception {
        // get user name, IP and port
        String title = "Java communicator";
        final String name = username;
        final int port = 6000;
        String IP = ip;

        // create gui
        final JFrame frame = new JFrame(title);
        JPanel contentPane = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setTabSize(2);
        final JTextField textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connected) {
                    sendMsg(textField.getText());
                    textField.setText("");
                }
            }
        });
        scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(textField, BorderLayout.SOUTH);
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendMsg("<-- has left a chat -->");
                MainWindow.closeconv();
                if (s != null) {
                    try {
                        s.close();
                        
                    } catch (IOException ex) {
                        //Logger.getLogger(TcpConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                frame.dispose();
                //System.exit(0);
            }
        });
        frame.setSize(300, 200);
        frame.setVisible(true);
        showIPs();

        // create sockets
        if (IP.equals("")) {
            (new Thread() {
                @Override
                public void run() {
                    Server.start(new String[] {"" + port});
                }
            }).start();
            Thread.sleep(1000);
            InetAddress thisIp = null;
			NetworkInterface ni = NetworkInterface.getByName("wlan0");    
	    	Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
	    	while(inetAddresses.hasMoreElements()) {  
	    	         InetAddress ia = inetAddresses.nextElement();  
	    	         if(!ia.isLinkLocalAddress()) {  
	    	             thisIp=ia;
	    	         }    
	    	} 
            s = new Socket(thisIp.getHostAddress(), port);
            log("Server set at port: " + port);
        } 
        else 
        {
            s = new Socket(IP, port);
        }
        connected = true;
        textField.requestFocusInWindow();

        // create streams
        out = new ObjectOutputStream(s.getOutputStream());
        out.flush();
        sendMsg(name);
        in = new ObjectInputStream(s.getInputStream());

        // wait for messages
        while (MainWindow.workerconv) {
            try {
                log(in.readObject().toString());
                Toolkit.getDefaultToolkit().beep();
            } catch (Exception ex) {
                //Logger.getLogger(TcpConnection.class.getName()).log(Level.SEVERE, null, ex);
                //MainWindow.closeconv();
                //frame.dispose();
                //System.exit(0);
            }
        }

    }

    private static void sendMsg(String msg) {
        if (out == null) {
            return;
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(TcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static synchronized void log(String msg) {
        textArea.append(msg + "\n");
        controllers.FileController.saveToFile(msg + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    private static void showIPs() {
        try {
        	NetworkInterface ni = NetworkInterface.getByName("wlan0");    
        	Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
        	while(inetAddresses.hasMoreElements()) {  
        	         InetAddress ia = inetAddresses.nextElement();  
        	         if(!ia.isLinkLocalAddress()) {  
        	             log("Your internal IP is : " + ia.getHostAddress());
        	         }    
        	}  
        	
            //InetAddress thisIp = InetAddress.getLocalHost();
            log("Your external IP is : " + getExternalIP());
        } catch (Exception ex) {
            Logger.getLogger(TcpConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getExternalIP() {
        try {
            URL whatismyip = new URL("http://checkip.dyndns.org:8245/");
            BufferedReader inIP = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return inIP.readLine().replace("<html><head><title>Current IP Check</title></head><body>Current IP Address: ", "").replace("</body></html>", "");
        } catch (Exception ex) {
            Logger.getLogger(TcpConnection.class.getName()).log(Level.SEVERE, null, ex);
            return "ex";
        }
    }
}