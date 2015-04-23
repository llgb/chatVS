package verteilteSysteme;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class ChatWindow {

	private JFrame frmChatsystemTinfb;
	private JTextField tfEingabe;
	private JTextPane paneMessages;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ChatWindow window = new ChatWindow();
					window.frmChatsystemTinfb.setVisible(true);
					window.addStringMessage("test line 1");
					window.addStringMessage("test line 2");

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
	public void addMessage(Message message) {		
		try {
			Document doc = paneMessages.getDocument();
			doc.insertString(doc.getLength(), message.toString() +"\n", null);
		} catch(BadLocationException exc) {
			exc.printStackTrace();
		}		
	}
	public void addStringMessage(String s) {		
		try {
			Document doc = paneMessages.getDocument();
			doc.insertString(doc.getLength(), s +"\n", null);
		} catch(BadLocationException exc) {
			exc.printStackTrace();
		}		
	}
	public void sendMessage(String message) {
		addStringMessage(message);
		tfEingabe.setText("");
		tfEingabe.requestFocusInWindow();
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

		paneMessages = new JTextPane();
		paneMessages.setEditable(false);
		paneMessages.setBounds(10, 11, 655, 489);
		frmChatsystemTinfb.getContentPane().add(paneMessages);

		tfEingabe = new JTextField();
		tfEingabe.setBounds(10, 511, 655, 39);
		frmChatsystemTinfb.getContentPane().add(tfEingabe);
		tfEingabe.setColumns(10);

		JButton btnSend = new JButton("Senden");
		btnSend.setBounds(675, 511, 149, 39);
		btnSend.addActionListener(new java.awt.event.ActionListener() {
			// Beim Dr�cken des Men�punktes wird actionPerformed aufgerufen
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//Aktion ausf�hren
				sendMessage(tfEingabe.getText());
			}
		});
		tfEingabe.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e){
				sendMessage(tfEingabe.getText());

			}});		
		frmChatsystemTinfb.getContentPane().add(btnSend);		
		JList listMembers = new JList();
		listMembers.setBounds(675, 11, 149, 489);
		frmChatsystemTinfb.getContentPane().add(listMembers);
		// Fokus in Eingabefeld setzen
		frmChatsystemTinfb.addWindowListener( new WindowAdapter() {
			@Override
			public void windowOpened( WindowEvent e ){
				tfEingabe.requestFocus();
			}
		});



	}

}
