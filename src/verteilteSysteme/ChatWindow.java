package verteilteSysteme;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JList;

public class ChatWindow {

	private JFrame frmChatsystemTinfb;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow();
					window.frmChatsystemTinfb.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatsystemTinfb = new JFrame();
		frmChatsystemTinfb.setTitle("ChatSystem TINF12B4");
		frmChatsystemTinfb.setBounds(100, 100, 850, 600);
		frmChatsystemTinfb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatsystemTinfb.getContentPane().setLayout(null);
		
		JTextPane paneMessages = new JTextPane();
		paneMessages.setEditable(false);
		paneMessages.setBounds(10, 11, 655, 489);
		frmChatsystemTinfb.getContentPane().add(paneMessages);
		
		textField_1 = new JTextField();
		textField_1.setBounds(10, 511, 655, 39);
		frmChatsystemTinfb.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnSend = new JButton("Senden");
		btnSend.setBounds(675, 511, 149, 39);
		frmChatsystemTinfb.getContentPane().add(btnSend);
		
		JList listMembers = new JList();
		listMembers.setBounds(675, 11, 149, 489);
		frmChatsystemTinfb.getContentPane().add(listMembers);
		
		
		
	}
}
