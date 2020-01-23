import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;
import game.Game;

public class Server {

	private JFrame jFrame;
	private ServerGameScreen serverGameScreen;

	private ServerSocket app;
	private Socket client;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Server() {
		jFrame = new JFrame("Server / Player 1");

		serverGameScreen = new ServerGameScreen(this);
		showGameScreen();
	}

	public void poll() {

		try {
			app = new ServerSocket(8080);
			client = app.accept();

			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
			sendGame(serverGameScreen.getGame());

			serverGameScreen.displayNotification("Connected to client", true, 4000);

			while (true) {
				serverGameScreen.changeOccured((Game) in.readObject());
			}

		} catch (Exception e) {
			serverGameScreen.displayNotification("Error connecting to client", false, 4000);
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
		updateScreen(serverGameScreen);
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
		Server server = new Server();

		server.poll();
	}
}