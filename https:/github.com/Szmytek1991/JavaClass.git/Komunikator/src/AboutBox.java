/*
 * AboutBox.java
 *
 * Created on __DATE__, __TIME__
 */

/**
 *
 * @author  __USER__
 */
public class AboutBox extends javax.swing.JFrame {
	String m_login;
	/** Creates new form AboutBox */
	public AboutBox(String Login) {
		m_login=Login;
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		this.setTitle("Logged as : " + m_login);

		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});
		addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				formFocusGained(evt);
			}
		});

		jTextArea1.setColumns(20);
		jTextArea1.setEditable(false);
		jTextArea1.setRows(5);
		jTextArea1
				.setText("This is a text Communicator\nMade for Programming in Java classes\nAt University of Science and Technology AGH\nBy Szmytke Pawel");
		jScrollPane1.setViewportView(jTextArea1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addGap(26, 26, 26)
						.addComponent(jScrollPane1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 307,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(44, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addGap(73, 73, 73)
						.addComponent(jScrollPane1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 130,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(96, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void formFocusGained(java.awt.event.FocusEvent evt) {
		// TODO add your handling code here:
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		this.dispose();
		mainwindow main = new mainwindow(m_login);
		main.show(true);
		// TODO add your handling code here:
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea jTextArea1;
	// End of variables declaration//GEN-END:variables

}