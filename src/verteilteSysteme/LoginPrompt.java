package verteilteSysteme;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPrompt {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	private JFrame frmLoginWindow;
	private JTextField tfUsername;
	private JButton btnLogin;
	private JLabel lblStatusText;
	private JLabel lblTFdescripton;
	private Border blackline;
	
	public LoginPrompt(){
		initialize();
		this.frmLoginWindow.setVisible(true);
	}
	
	public  void initialize () {
		
		
		/*
		 *   Draw Login Prompt like this
		 *   ___________________________________
		 *   |									|
		 *   |		Username	[___________]	|
		 *   |									|
		 *   |	 	________________________	|
		 *   |		|		Einloggen		|	|
		 *   |		-------------------------	|
		 *   |		Statustext here.....		|
		 *   ------------------------------------
		 */
		frmLoginWindow = new JFrame();
		frmLoginWindow.setTitle("chatVS Anmeldung");
		frmLoginWindow.setBounds(100, 100, 300, 120);
		frmLoginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLoginWindow.getContentPane().setLayout(null);
		frmLoginWindow.setResizable(false);
		
		tfUsername = new JTextField();
		tfUsername.setBounds(150,5,135,25);
		frmLoginWindow.add(tfUsername);
		
		lblTFdescripton = new JLabel();
		lblTFdescripton.setBounds(5,10,100,10);
		lblTFdescripton.setText("Username:");
		frmLoginWindow.add(lblTFdescripton);
		
		btnLogin = new JButton();
		btnLogin.setBounds(5,35,280,30);
		btnLogin.setText("Einloggen");
		frmLoginWindow.add(btnLogin);
		btnLogin.addActionListener(new java.awt.event.ActionListener() {
					// Beim Drücken des Menüpunktes wird actionPerformed aufgerufen
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						// Aktion ausführen
						login(tfUsername.getText());
					}
				});
		lblStatusText = new JLabel();
		lblStatusText.setBounds(5,70,280,20);
		lblStatusText.setForeground(Color.BLACK);
		//lblStatusText.setFont(new Font("Comic Sans MS",Font.PLAIN, 14)); //lieber doch kein ComicSans ;)
		//blackline = BorderFactory.createLineBorder(Color.black);
		blackline = BorderFactory.createLoweredBevelBorder();
		lblStatusText.setBorder(blackline);
		lblStatusText.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatusText.setText("Bitte gewünschten Usernamen eingeben");
		frmLoginWindow.add(lblStatusText);
		
		
		
		
	}
	public void login(String username) {
		if (true) {
			logger.info("do login");
		}
		else {
			lblStatusText.setForeground(Color.RED);
			lblStatusText.setText("Username ist leider schon vergeben");			
		}
	}
	public static void main(String[] args) {
	LoginPrompt lp = new LoginPrompt();
	
		
	}

}
