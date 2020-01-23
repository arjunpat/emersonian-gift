
public class Runner {
	public static void main(String[] args) {

		Thread s = new Thread(new Runnable() {
			public void run() {
				Server server = new Server();

				server.poll(); 
			}
		});

		Thread c = new Thread(new Runnable() {
			public void run() {
				Client client = new Client();

				client.poll();
			}
		});

		s.start();
		try {Thread.sleep(1000);} catch (Exception e) { e.printStackTrace(); }
		c.start();

	}
}