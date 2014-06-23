package windows;


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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import models.Server;
import windows.MainWindow;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JInternalFrame;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridLayout;
import javax.swing.JButton;

public class TcpConnection {
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;
    private static JTextArea textArea = null;
    private static JScrollPane scrollPane = null;
    private static boolean connected = false;
    private static Socket s = null;
    private static JLabel lblPort = null;
    private static JLabel lblIntIP = null;
    private static JLabel lblExtIP = null;

    /**
     * @wbp.parser.entryPoint
     */
    public static void start(String username, String ip) throws Exception {
        String title = "Network Communicator : " + username;
        final String name = username;
        final int port = 6000;
        String IP = ip;

        final JFrame frame = new JFrame(title);
        JPanel contentPane = new JPanel(new BorderLayout());
        textArea = new JTextArea();


        lblPort = new JLabel("Port :");
        lblIntIP = new JLabel("Internal IP :");
        lblExtIP = new JLabel("External IP :");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setTabSize(2);
        scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        frame.setContentPane(contentPane);
        
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(0, 1, 0, 0));
        
        lblPort.setVerticalAlignment(SwingConstants.TOP);
        lblPort.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(lblPort);
        panel.add(lblIntIP);
        
        panel.add(lblExtIP);
        
        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new GridLayout(2, 0, 0, 0));
        final JTextField textField = new JTextField();
        panel_1.add(textField);
        
        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
                if (connected) {
                    sendMessage(textField.getText());
                    textField.setText("");
                }
        		
        	}
        });
        panel_1.add(btnSend);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connected) {
                    sendMessage(textField.getText());
                    textField.setText("");
                }
            }
        });
        textField.requestFocusInWindow();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.closeconv();
                if (s != null) {
                    try {
                        s.close();
                        
                    } catch (IOException ex) {
                    }
                }
                frame.dispose();
            }
        });
        frame.setSize(400, 400);
        frame.setVisible(true);
        showIPs();

        if (IP.equals("")) {
            (new Thread() {
                @Override
                public void run() {
                    Server.start(Integer.toString(port));
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

            lblPort.setText("Port : " + port);
        } 
        else 
        {
            s = new Socket(IP, port);
        }
        connected = true;

        out = new ObjectOutputStream(s.getOutputStream());
        out.flush();
        sendMessage(name);
        in = new ObjectInputStream(s.getInputStream());

        while (MainWindow.workerconv) {
            try {
                logToScreen(in.readObject().toString());
                Toolkit.getDefaultToolkit().beep();
            } catch (Exception ex) {
            }
        }

    }

    private static void sendMessage(String msg) {
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

    private static synchronized void logToScreen(String msg) {
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
        	             lblIntIP.setText("Internal IP : " + ia.getHostAddress());
        	         }    
        	}  

            lblExtIP.setText("External IP : " + getExternalIP());
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