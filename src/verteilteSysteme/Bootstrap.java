package verteilteSysteme;

public class Bootstrap {
	public static void main(String[] args) {

		try {
			ChatWindow window = new ChatWindow("klaus1");
			Thread pollthread;
			pollthread = new Thread(new PollThread(window));
			pollthread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
