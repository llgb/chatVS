package verteilteSysteme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatWindow {

	private JFrame frmChatsystemTinfb;
	private JTextField tfEingabe;
	private JTextPane paneMessages;
	private JScrollPane scrollPane;
	private ChatWindow window;
	private List<Message> messagelist;
	private MySQLAccess dao;
	private User user;

	private static final Logger logger = LoggerFactory
			.getLogger(ChatWindow.class);

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public ChatWindow(String username) throws Exception {
		initialize();
		this.frmChatsystemTinfb.setVisible(true);
		this.dao = new MySQLAccess();
		this.user = new User(username);
	}

	public void addSingleMessage(Message message) {
		try {
			Document doc = paneMessages.getDocument();
			doc.insertString(doc.getLength(), message.toString() + "\n", null);
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	public void addMessageList(List<Message> messageList) {
		for (int i = messageList.size() - 1; i >= 0; i--) {
			addSingleMessage(messageList.get(i));
		}
	}

	public void addStringMessage(String s) { // für Nachricht direkt zum UI
												// hinzu
		try {
			Document doc = paneMessages.getDocument();
			doc.insertString(doc.getLength(), s + "\n", null);
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	public void sendMessage(String messageString) {
		try {
			this.dao.writeToDataBase(new Message(user, messageString));
			tfEingabe.setText("");
			tfEingabe.requestFocusInWindow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		frmChatsystemTinfb.setResizable(false);

		paneMessages = new JTextPane();
		paneMessages.setEditable(false);
		scrollPane = new JScrollPane();
		JScrollPane messagesScrollPane = new JScrollPane(paneMessages);
		messagesScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		messagesScrollPane.setBounds(10, 11, 655, 489);

		frmChatsystemTinfb.getContentPane().add(messagesScrollPane);

		tfEingabe = new JTextField();
		tfEingabe.setBounds(10, 511, 655, 39);
		frmChatsystemTinfb.getContentPane().add(tfEingabe);
		tfEingabe.setColumns(10);

		JButton btnSend = new JButton("Senden");
		btnSend.setBounds(675, 511, 149, 39);
		btnSend.addActionListener(new java.awt.event.ActionListener() {
			// Beim Drücken des Menüpunktes wird actionPerformed aufgerufen
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				// Aktion ausführen
				sendMessage(tfEingabe.getText());
			}
		});
		tfEingabe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(tfEingabe.getText());

			}
		});
		frmChatsystemTinfb.getContentPane().add(btnSend);
		JList listMembers = new JList();
		JScrollPane membersScrollPane = new JScrollPane(listMembers);
		membersScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		membersScrollPane.setBounds(675, 11, 149, 489);
		frmChatsystemTinfb.getContentPane().add(membersScrollPane);
		// Fokus in Eingabefeld setzen
		frmChatsystemTinfb.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				tfEingabe.requestFocus();
			}
		});

	}

}
