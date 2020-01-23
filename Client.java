import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;
import game.Game;

public class Client {

	private JFrame jFrame;
	private ClientGameScreen clientGameScreen;

	private Socket conn;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client() {
		jFrame = new JFrame("Client / Player 2");
	}

	public void poll() {

		try {
			conn = new Socket("localhost", 8080);

			out = new ObjectOutputStream(conn.getOutputStream());
			in = new ObjectInputStream(conn.getInputStream());
			
			clientGameScreen = new ClientGameScreen(this);
			showGameScreen();

			clientGameScreen.displayNotification("Connected to server", true, 4000);

			while (true) {
				clientGameScreen.changeOccured((Game) in.readObject());
			}

		} catch (Exception e) {
			clientGameScreen.displayNotification("Error connecting to server", false, 4000);
		}
	}

	public void sendGame(Game game) {
		try {
			out.reset();
			out.writeObject(game);
			
			game.setStatus("");
		} catch (Exception err) {
			System.out.println(err);
		}
	}

	private void showGameScreen() {
		updateScreen(clientGameScreen);
	}

	private void updateScreen(View view) {
		jFrame.setContentPane(view);
		jFrame.pack();
		jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		jFrame.setVisible(true);
	}

	public void lose(String why) {
		updateScreen(new ResultScreen(false, why));
	}

	public void win(String why) {
		updateScreen(new ResultScreen(true, why));
	}

	public static void main(String[] args) {
		Client client = new Client();

		client.poll();
	}
}