package windows;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JOptionPane;

import threads.ActivityThread;
import models.TcpConnection;

import controllers.DbController;
import controllers.WindowController;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MainWindow extends javax.swing.JFrame {

	Thread actthread;
	Thread convthrd;
	static public boolean workerconv;
	static String m_loggedas;
	static String m_ip;
	static boolean worker = true;
	static boolean worker1 = true;
	static List<String> friends = new ArrayList<String>();
	static List<Integer> loggedin = new ArrayList<Integer>();
	static List<String> conv = new ArrayList<String>();
	static int convstarted = 0;
	static String convfriend = "";

	/** Creates new form mainwindow */
	public MainWindow(String loggedas) {

		m_loggedas = loggedas;
		worker = true;
		java.sql.Connection conn = DbController.dbconnect();
		friends = DbController.getfriends(conn, loggedas);
		loggedin = DbController.getactivity(conn, loggedas);
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initComponents();
		int length;
		length = friends.size();
		for (int i = 0; i < length; i++) {
			jTable1.setValueAt(friends.get(i), i, 0);
		}
		length = loggedin.size();
		for (int i = 0; i < length; i++) {
			if (loggedin.get(i) == 0)
				jTable1.setValueAt("Not LoggedIn", i, 1);
			else
				jTable1.setValueAt("LoggedIn", i, 1);
		}

		actthread = new Thread(new ActivityThread(), "thread1");
		actthread.start();
		convthrd = new Thread(new threads.ConversationThread(), "thread1");
		convthrd.start();
	}

	public static void activitythread() {
		while (worker) {
			java.sql.Connection conn = DbController.dbconnect();
			loggedin = DbController.getactivity(conn, m_loggedas);
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int length = loggedin.size();
			for (int i = 0; i < length; i++) {
				if (loggedin.get(i) == 0)
					jTable1.setValueAt("Not LoggedIn", i, 1);
				else
					jTable1.setValueAt("LoggedIn", i, 1);
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void conversationthread() throws Exception {
		while (worker1) {
			java.sql.Connection conn = DbController.dbconnect();
			conv = DbController.getconversation(conn, m_loggedas);
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int length = conv.size();
			for (int i = 0; i < length; i++) {
				if ((conv.get(i) != null))
				{

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (convstarted == 0) {
						startconversation();
						
					}
				}
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void initComponents() {

		desktopPane = new javax.swing.JDesktopPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		exitMenuItem = new javax.swing.JMenuItem();
		jMenuItem1 = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		aboutMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(3);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		desktopPane.setName("");

		jTable1.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null } }, new String[] {
						"User", "Status" }) {
			@SuppressWarnings("rawtypes")
			Class[] types = new Class[] { java.lang.String.class,
					java.lang.String.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
		});
		jScrollPane1.setViewportView(jTable1);

		jScrollPane1.setBounds(10, 0, 210, 280);
		desktopPane.add(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jButton1.setText("Add Friend");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		jButton1.setBounds(230, 30, 160, 31);
		desktopPane.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jButton2.setText("Start Conversation");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jButton2ActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		jButton2.setBounds(230, 220, 160, 31);
		desktopPane.add(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);

		fileMenu.setText("File");

		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		jMenuItem1.setText("Change User");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		fileMenu.add(jMenuItem1);

		menuBar.add(fileMenu);

		helpMenu.setText("Help");

		aboutMenuItem.setText("About");
		aboutMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				aboutMenuItemMouseClicked(evt);
			}
		});
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 279,
				Short.MAX_VALUE));
		
		JButton btnHistory = new JButton("Show History");
		btnHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controllers.WindowController.showhistorywindow();
			}
		});
		btnHistory.setBounds(232, 93, 158, 25);
		desktopPane.add(btnHistory);
		
		JButton btnNewButton = new JButton("Clean History");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controllers.FileController.cleanHistory();
			}
		});
		btnNewButton.setBounds(232, 150, 158, 25);
		desktopPane.add(btnNewButton);

		pack();
	}// </editor-fold>
	//GEN-END:initComponents
	private static void startconversation() throws SocketException
	{
		workerconv=true;
		String ip="";
		java.sql.Connection conn = DbController.dbconnect();
		conv = DbController.getconversation(conn, m_loggedas);
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length = conv.size();
		for (int i = 0; i < length; i++) {
			if (!(conv.get(i) == null))
			{
					ip = conv.get(i);
					m_ip = ip;
		            (new Thread() {
		                @Override
		                public void run() {
		                    try {
		                    	if(workerconv)
		                    		TcpConnection.start(m_loggedas,m_ip);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                }
		            }).start();
		            convstarted = 1;
			}
			else
			{

				InetAddress thisIp = null;
				NetworkInterface ni = NetworkInterface.getByName("wlan0");    
		    	Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
		    	while(inetAddresses.hasMoreElements()) {  
		    	         InetAddress ia = inetAddresses.nextElement();  
		    	         if(!ia.isLinkLocalAddress()) {  
		    	             thisIp=ia;
		    	         }    
		    	} 
		    	convfriend = friends.get(jTable1.getSelectedRow());
				DbController.startconv(convfriend, thisIp.getHostAddress());

	            (new Thread() {
	                @Override
	                public void run() {
	                    try {
	                    	if(workerconv)
							TcpConnection.start(m_loggedas,"");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                }
	            }).start();
	            convstarted = 1;
				//TcpConnection.start(m_loggedas,"");
				
			}
				
					
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		if(convstarted==1)
		{
			JOptionPane.showMessageDialog(null, "Conversation is running, close current conversation first!!");
		}
		else
		{
			if(loggedin.get(jTable1.getSelectedRow())==0)
			{
				JOptionPane.showMessageDialog(null, "This user is offline!");
			}
			else
				startconversation();
		}
		
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		DbController.logout(m_loggedas);
		worker = false;
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		WindowController.showaddfriendwindow(m_loggedas);
		WindowController.closemainwindow();
		worker = false;
	}

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		DbController.logout(m_loggedas);
		WindowController.showloginwindow();
		WindowController.closemainwindow();
		worker = false;
	}

	private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		WindowController.showaboutbox(m_loggedas);
		WindowController.closemainwindow();
		worker = false;
	}

	private void aboutMenuItemMouseClicked(java.awt.event.MouseEvent evt) {

	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		DbController.logout(m_loggedas);
		worker = false;
		System.exit(0);
	}//GEN-LAST:event_exitMenuItemActionPerformed

	public static void closeconv()
	{

		DbController.startconv(convfriend, null);
		workerconv = false;
		convstarted = 0;
	}
	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JDesktopPane desktopPane;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JScrollPane jScrollPane1;
	private static javax.swing.JTable jTable1;
	private javax.swing.JMenuBar menuBar;
}