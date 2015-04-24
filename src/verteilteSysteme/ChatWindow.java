package verteilteSysteme;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatWindow {

	private JFrame frmChatsystemTinfb;
	private JTextField tfEingabe;
	private JTextPane paneMessages;
	private JScrollPane membersScrollPane;
	private JList listMembers;
	private DefaultListModel listModelMembers;
	private ChatWindow window;
	private List<Message> messagelist;
	private MySQLAccess dao;
	private User user;
	private JScrollPane messagesScrollPane;

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
		addMemberToList(this.user);
		loadMemberList();
	}
	public ChatWindow(String server, String dbusername, String dbpassword, String username) throws Exception {
		initialize();
		this.frmChatsystemTinfb.setVisible(true);
		this.dao = new MySQLAccess(server, dbusername, dbpassword);
		this.user = new User(username);
		addMemberToList(this.user);
		loadMemberList();
	}

	public void addSingleMessage(Message message) {
		try {
			Document doc = paneMessages.getDocument();
			StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet aset;
			JScrollBar vertical;
			if (message.getOwner().getUsername().equals(this.user.getUsername())) {
				aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(255,0,0));
			}
			else{
				aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0,0,0));
			}
			doc.insertString(doc.getLength(), message.toString() + "\n", aset);
			
			paneMessages.setCaretPosition(paneMessages.getDocument().getLength());
			
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

	public void loadMemberList() {
		try {
			List<User> dbCurrentUsers = new ArrayList<User>();
			dbCurrentUsers = this.dao.getCurrentUsers();
			for (User user : dbCurrentUsers) {
				this.listModelMembers.addElement(user.getUsername());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addMemberToList(User user) {
		try {
			this.dao.addUserToDB(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addMemberListToList(List<User> newUsers) {
		this.listModelMembers.clear();
		for (User user : newUsers) {
			this.listModelMembers.addElement(user.getUsername());
		}

	}

	public void removeMemberFromList(User user) {
		try {
			this.dao.removeUserfromDB(user);
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
		frmChatsystemTinfb.setTitle("ChatSystem TINF12B4 username: "+ this.user.getUsername()+" server: " + this.dao.getServer());
		frmChatsystemTinfb.setBounds(100, 100, 850, 600);
		frmChatsystemTinfb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatsystemTinfb.getContentPane().setLayout(null);
		frmChatsystemTinfb.setResizable(false);
		frmChatsystemTinfb.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				removeMemberFromList(user);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		;
		paneMessages = new JTextPane();
		paneMessages.setMargin(new Insets(5, 5, 5, 5));
		paneMessages.setEditable(false);
		messagesScrollPane = new JScrollPane(paneMessages);
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
		listModelMembers = new DefaultListModel();
		listMembers = new JList(listModelMembers);
		membersScrollPane = new JScrollPane(listMembers);
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
