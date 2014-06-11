package windows;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.DropMode;
import javax.swing.JTextArea;

public class History extends JFrame {

	private JPanel contentPane;
	private JTextArea txtrTest = new JTextArea();

	public History() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JScrollPane scrollPane = new JScrollPane(txtrTest);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		txtrTest.setEditable(false);
		txtrTest.setText(controllers.FileController.readFile());
	}
}
