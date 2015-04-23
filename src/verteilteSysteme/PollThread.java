package verteilteSysteme;

import java.sql.SQLException;
import java.util.ArrayList;

public class PollThread {
	private Thread thread;
	private int numberOfMessages=0;
	private MySQLAccess dao;
	ArrayList<Message> loadMessages = new ArrayList<Message>();
	
	public PollThread(ChatWindow chatfenster) throws Exception {
		
		super();
		dao = new MySQLAccess();
		 
		this.thread = new Thread(new Runnable(){
			@Override
	            public void run() {
	                try {
	                	while(true){
	                		int currentInDBMessages = dao.countMessages();	                		
	                		int messageDifference = currentInDBMessages - numberOfMessages;
	                		System.out.println("Nachrichtenanzahl beträgt: " + currentInDBMessages);
	                		System.out.println("Nachrichtendifferenz beträgt: " + messageDifference);
	                		if (messageDifference > 0 ) {
	                			loadMessages = dao.getLatestMessages(messageDifference);
	                			numberOfMessages = loadMessages.size();
	                			chatfenster.addMessageList(loadMessages);
	                		}
	                		Thread.sleep(500);
	                	}
						
					} catch (InterruptedException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
		});
		thread.start();
	}
	

}
