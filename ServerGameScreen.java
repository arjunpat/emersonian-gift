import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import items.Player;
import items.Notification;
import game.Game;
import game.Location;

import javax.swing.Timer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.Map.Entry;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ServerGameScreen extends View {

	private Game game;
	private Server server;

	private Notification notification = new Notification("", false, 0);

	private Player player;
	private Player clientPlayer;
	private HashMap<String, Integer> itemsCollectedMap = new HashMap<String, Integer>();
	private Stack<Boolean> healthStack = new Stack<Boolean>();
	private BufferedImage background;

	private HashMap<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();

	private double progress = .01;
	private int titleStatus = 0;
	
	public ServerGameScreen(Server server) {
		game = new Game();
		this.server = server;

		try {
			background = ImageIO.read(getClass().getResource("items/images/board_red.jpeg"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Location l = game.getClientLocation();
		clientPlayer = new Player(l.getX(), l.getY(), "images/robot2.png");
		l = game.getServerLocation();
		player = new Player(l.getX(), l.getY(), "images/robot1.png");

		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				if (keyCode == 40) {
					player.moveUp();
				} else if (keyCode == 38) {
					player.moveDown();
				} else if (keyCode == 39) {
					player.moveRight();
				} else if (keyCode == 37) {
					player.moveLeft();
				} else if (keyCode == 32) { // spacebar, place bomb

					Location l = getCurrentLocation();

					if (!game.getMap().containsKey(l) && itemsCollectedMap.get("Bombs left") > 0) {
						game.getMap().put(l, 2);
						game.setStatus("bomb");

						itemsCollectedMap.put("Bombs left", itemsCollectedMap.get("Bombs left") - 1);

					} else {
						displayNotification("You cannot place a bomb here", false);
					}

					server.sendGame(game);

					repaint();

					return;
				}

				checkHit();
				repaint();

				game.setServerLocation(new Location(player.getX(), player.getY()));
				server.sendGame(game);
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});

		for (int i = 0; i < 5; i++) {
			healthStack.push(true);
		}

		itemsCollectedMap.put("Bombs left", 5);


		Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();

		Thread t = new Thread(new Runnable() {
			public void run() {
				try { Thread.sleep(1000); } catch (Exception e) {}
				while (progress < 1) {
					progress += .01;
					try {
						Thread.sleep(20);
					} catch (Exception e) {}
					repaint();
				}
				while (titleStatus < 5) {
					try { Thread.sleep(1000); } catch (Exception e) {}
					titleStatus++;
					repaint();
				}
			}
		});

		t.start();
	}

	public void draw(Graphics g) {

		g.drawImage(background, 0, 0, null);

		Iterator<Entry<Location,Integer>> it = game.getMap().entrySet().iterator();

		while (it.hasNext()) {
			Entry<Location, Integer> e = it.next();
			int itemType = e.getValue();
			Location l = e.getKey();

			BufferedImage pic;

			if (images.containsKey(itemType)) {
				pic = images.get(itemType);
			} else {
				pic = game.getImage(itemType);
				images.put(itemType, pic);
			}

			g.drawImage(pic, l.getX() * 100, l.getY() * 100, null);
		}

		clientPlayer.draw(g);
		player.draw(g);

		g.setColor(new Color(0, 0, 255, 220));
		g.fillRect(650, 0, 150, 180);
		g.setFont(new Font("Tahoma", Font.BOLD, 14));
		g.setColor(Color.WHITE);
		g.drawString("My health: " + healthStack.size(), 660, 20);
		g.drawString("Items collected: " + player.getItemsCollected(), 660, 40);

		Iterator<Entry<String, Integer>> i = itemsCollectedMap.entrySet().iterator();

		int y = 60;
		while (i.hasNext()) {
			Entry<String, Integer> e = i.next();

			g.drawString(e.getKey() + ": " + e.getValue(), 660, y);

			y += 20;
		}

		if (!notification.isDone())
			notification.draw(g);

		if (notification.isOld()) {
			notification.addASecond();
			animateNotificationDown();
		}

		// into text
		if (titleStatus < 5) {
			g.setColor(Color.BLACK);
			g.fillRoundRect((int)(100 * progress), (int)(50 * progress), (int)(600 / progress), (int)(230 / progress), 12, 12);
			g.setColor(Color.WHITE);
		}
		if (titleStatus == 0) {
			g.setFont(new Font("Tahoma", Font.BOLD, 28));
			g.drawString("Welcome to", 300, 125);
			g.setFont(new Font("Tahoma", Font.BOLD, 48));
			g.drawString("Triple Offence", 175, 175);
			g.setFont(new Font("Tahoma", Font.BOLD, 20));
			g.drawString("a game for Anirudh Kumar", 250, 210);
		} else if (titleStatus == 1) {
			g.setFont(new Font("Tahoma", Font.BOLD, 100));
			g.drawString("3", 350, 200);
		} else if (titleStatus == 2) {
			g.setFont(new Font("Tahoma", Font.BOLD, 100));
			g.drawString("2", 350, 200);
		} else if (titleStatus == 3) {
			g.setFont(new Font("Tahoma", Font.BOLD, 100));
			g.drawString("1", 350, 200);
		} else if (titleStatus == 4) {
			g.setFont(new Font("Tahoma", Font.BOLD, 100));
			g.drawString("Start!", 250, 200);
		}
			
	}

	private void checkHit() {
		Location l = getCurrentLocation();

		if (game.getMap().containsKey(l)) {

			int itemType = game.getMap().get(l);
			if (itemType == 1 || itemType == 2) {

				if (healthStack.size() > 1) {
					healthStack.pop();
					player.moveTo(520, 510);

					displayNotification("You touched an obstacle and lost a health", false);
					game.setStatus("health");
					playOofSound();
				} else {
					server.lose("You ran out of health!");
					game.setStatus("lost");
				}

				if (itemType == 2) {
					game.getMap().remove(l);
				}

			} else {
				String key = game.getObjectName(game.getMap().get(l));
				if (itemsCollectedMap.containsKey(key)) {
					itemsCollectedMap.put(key, itemsCollectedMap.get(key) + 1);
				} else {
					itemsCollectedMap.put(key, 1);
				}

				game.getMap().remove(l);
				player.itemCollected();
				if (game.itemCollected()) {
					game.setStatus("done");
					displayResults();
				} else {
					game.setStatus("collected");
					playDingSound();
				}
			}
		}
	}

	private void playDingSound() { playSound("items/sounds/ding.wav"); }
	private void playOofSound() { playSound("items/sounds/oof.wav"); }

	private void playSound(String filename) {
		try {
			URL url = this.getClass().getClassLoader().getResource(filename);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void displayNotification(String text, boolean isGood) {
		notification = new Notification(text, isGood);
		animateNotificationUp();
	}

	public void displayNotification(String text, boolean isGood, int time) {
		notification = new Notification(text, isGood, time);
		animateNotificationUp();
	}

	private void animateNotificationUp() {
		notification.setY(800);

		Thread animate = new Thread(new Runnable() {
			public void run() {

				while (notification.getY() > Notification.FINAL_Y) {
					try {
						Thread.sleep(Notification.ANIMATE_WAIT_TIME);
						notification.moveUp();
						repaint();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

		animate.start();
	}

	private void animateNotificationDown() {

		Thread animate = new Thread(new Runnable() {
			public void run() {

				while (notification.getY() < 800) {
					try {
						Thread.sleep(Notification.ANIMATE_WAIT_TIME);
						notification.moveDown();
						repaint();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				notification.done();

			}
		});

		animate.start();
	}

	private void displayResults() {
		int collected = player.getItemsCollected();

		if (collected > 6) {
			server.win("You collected " + collected + " of the 13 items!");
		} else {
			server.lose("You collected " + collected + " of the 13 items!");
		}
	}

	private Location getCurrentLocation() {
		int x = (int)(player.getX() / 100);
		int y = (int)(player.getY() / 100);

		return new Location(x, y);
	}

	public Game getGame() {
		return game;
	}

	public void changeOccured(Game game) {
		this.game = game;

		Location l = game.getClientLocation();
		clientPlayer.moveTo(l.getX(), l.getY());

		String status = game.getStatus();
		if (status.equals("lost")) {
			server.win("Your opponent ran out of health");
		} else if (status.equals("health")) {
			displayNotification("Your opponent lost a health", true);
		} else if (status.equals("collected")) {
			displayNotification("Your opponent collected an item", false);
		} else if (status.equals("bomb")) {
			displayNotification("Your opponent has placed a bomb", false);
		} else if (status.equals("done")) {
			displayResults();
		}

		repaint();
	}
}