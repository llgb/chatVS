package verteilteSysteme;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import verteilteSysteme.couchdb.Message;
import verteilteSysteme.couchdb.MessageRepository;
import verteilteSysteme.couchdb.ServerManager;
import verteilteSysteme.couchdb.User;
import verteilteSysteme.couchdb.UserRepository;
import verteilteSysteme.couchdb.connection.MessageCouchDbConnection;
import verteilteSysteme.couchdb.connection.UserCouchDbConnection;

/**
 * The application's main window.
 */
public class ChatWindow {
	private JFrame frmChatsystemTinfb;
	private JTextField tfEingabe;
	private JTextPane paneMessages;
	private JScrollPane membersScrollPane;
	private JList listMembers;
	private DefaultListModel listModelMembers;
	private User user;
	private JScrollPane messagesScrollPane;
	private final ServerManager serverManager;

	private static final Logger logger = LoggerFactory.getLogger(ChatWindow.class);

	/**
	 * Create the application window.
	 * 
	 * @throws Exception something crashed
	 */
	public ChatWindow(final String username, final ServerManager serverManager) throws Exception {
		logger.info("Creating and initializing the main window for user {}", username);
		this.serverManager = serverManager;
		this.user = new User(username);
		initialize();
		this.frmChatsystemTinfb.setVisible(true);
		addMemberToList(this.user);
		loadMemberList();
	}

	/**
	 * Add a single message to the main pane.
	 * 
	 * @param message the message to add
	 */
	public void addSingleMessage(final Message message) {
		try {
			final Document doc = paneMessages.getDocument();
			final StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet aset;
			
			if (message.getOwner().equals(this.user.getName())) {
				aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(255,0,0));
			} else {
				aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0,0,0));
			}
			
			doc.insertString(doc.getLength(), message.toString() + "\n", aset);
			paneMessages.setCaretPosition(paneMessages.getDocument().getLength());	
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Add multiple messages to the main pane.
	 * 
	 * @param messages the messages to add
	 */
	public void addMessageList(List<Message> messages) {
		for (Message message : messages) {
			addSingleMessage(message);
		}
	}

	/**
	 * Adds a string to the main pane.
	 * 
	 * @param message the string to add
	 */
	public void addStringMessage(final String message) {
		try {
			final Document doc = paneMessages.getDocument();
			doc.insertString(doc.getLength(), message + "\n", null);
		} catch (BadLocationException exc) {
			logger.error("Failed to insert the message into the pane.");
		}
	}

	/**
	 * Sends a message to the chat server.
	 * 
	 * @param message the message to send
	 */
	public void sendMessage(String message) {
		final MessageRepository messageRepository = new MessageRepository(MessageCouchDbConnection.get());
		messageRepository.add(new Message(this.user.getName(), message, new DateTime()));
		
		// Clear input field.
		tfEingabe.setText("");
		tfEingabe.requestFocusInWindow();
	}

	/**
	 * Fill the user list.
	 */
	public void loadMemberList() {
		final UserRepository userRepository = new UserRepository(UserCouchDbConnection.get());
		final List<User> dbCurrentUsers = userRepository.getAll();
		for (User user : dbCurrentUsers) {
			this.listModelMembers.addElement(user.getName());
		}
	}

	/**
	 * Add a single user to the users list.
	 * 
	 * @param user the user to add
	 */
	public void addMemberToList(final User user) {
		final UserRepository userRepository = new UserRepository(UserCouchDbConnection.get());
		userRepository.add(user);
	}

	/**
	 * Add multiple users to the users list.
	 * 
	 * @param users the users to add
	 */
	public void addMemberListToList(List<User> users) {
		this.listModelMembers.clear();
		for (User user : users) {
			this.listModelMembers.addElement(user.getName());
		}
	}

	/**
	 * Remove a user from the users list.
	 * 
	 * @param user the user to remove
	 */
	public void removeMemberFromList(final User user) {
		final UserRepository userRepository = new UserRepository(UserCouchDbConnection.get());
		userRepository.remove(user);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmChatsystemTinfb = new JFrame();
		this.frmChatsystemTinfb.setTitle("ChatSystem TINF12B4 username: "+ this.user.getName() + " Server: " + MessageCouchDbConnection.getHost());
		this.frmChatsystemTinfb.setBounds(100, 100, 850, 600);
		this.frmChatsystemTinfb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frmChatsystemTinfb.getContentPane().setLayout(null);
		this.frmChatsystemTinfb.setResizable(false);
		this.frmChatsystemTinfb.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				removeMemberFromList(user);
				serverManager.writeHostListToDisk();
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		this.paneMessages = new JTextPane();
		this.paneMessages.setMargin(new Insets(5, 5, 5, 5));
		this.paneMessages.setEditable(false);
		this.messagesScrollPane = new JScrollPane(this.paneMessages);
		this.messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.messagesScrollPane.setBounds(10, 11, 655, 489);

		this.frmChatsystemTinfb.getContentPane().add(this.messagesScrollPane);

		this.tfEingabe = new JTextField();
		this.tfEingabe.setBounds(10, 511, 655, 39);
		this.frmChatsystemTinfb.getContentPane().add(this.tfEingabe);
		this.tfEingabe.setColumns(10);

		JButton btnSend = new JButton("Senden");
		btnSend.setBounds(675, 511, 149, 39);
		btnSend.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				sendMessage(tfEingabe.getText());
			}
		});
		this.tfEingabe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(tfEingabe.getText());
			}
		});
		
		this.frmChatsystemTinfb.getContentPane().add(btnSend);
		this.listModelMembers = new DefaultListModel();
		this.listMembers = new JList(listModelMembers);
		this.membersScrollPane = new JScrollPane(listMembers);
		this.membersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.membersScrollPane.setBounds(675, 11, 149, 489);
		this.frmChatsystemTinfb.getContentPane().add(this.membersScrollPane);
		
		// Fokus in Eingabefeld setzen
		this.frmChatsystemTinfb.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				tfEingabe.requestFocus();
			}
		});
	}
}
