package game;

import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.Serializable;
import java.util.Stack;

public class Game implements Serializable {
	private HashMap<Location, Integer> map;

	private Location serverLocation;
	private Location clientLocation;
	private String status = "";

	private int itemsCollected;

	private String[] values = {
		"Disk",
		"Wall",
		"Ball",
		"Ball",
		"Phone",
		"Drink"
	};

	private String[] images = {
		"../items/images/disk.png",
		"../items/images/wall.png",
		"../items/images/ball.png",
		"../items/images/ball.png",
		"../items/images/phone.png",
		"../items/images/drink.png"
	};

	public Game() {

		serverLocation = new Location(320, 610);
		clientLocation = new Location(320, 600);

		map = new HashMap<Location, Integer>();
		
		// wall
		/*map.put(new Location(0, 2), 1);
		map.put(new Location(1, 2), 1);
		map.put(new Location(2, 2), 1);
		map.put(new Location(2, 3), 1);
		map.put(new Location(2, 4), 1);
		map.put(new Location(2, 5), 1);
		map.put(new Location(3, 2), 1);
		map.put(new Location(4, 2), 1);
		map.put(new Location(5, 2), 1);
		map.put(new Location(6, 2), 1);*/

		map.put(new Location(5, 5), 3);

		// randomly generated items
		Stack<Integer> items = new Stack<Integer>();

		items.push(0); items.push(0); items.push(0); items.push(0);
		items.push(0); items.push(0); items.push(3); items.push(3);
		items.push(3); items.push(3); items.push(3); items.push(3);
		items.push(3);

		while (!items.isEmpty()) {
			int itemNum = items.pop();

			while (true) {

				Location l = new Location((int)(Math.random() * 8), (int)(Math.random() * 8));

				if (!map.containsKey(l)) {
					map.put(l, itemNum);
					break;
				}
			}
		}

	}

	public HashMap<Location, Integer> getMap() {
		return map;
	}

	public BufferedImage getImage(int i) {
		String filename = images[i];

		try {
			return ImageIO.read(getClass().getResource(filename));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String toString() {
		return "[Game class]";
	}

	public String getObjectName(int i) {
		return values[i];
	}

	public void setServerLocation(Location l) {
		serverLocation = l;
	}

	public void setClientLocation(Location l) {
		clientLocation = l;
	}

	public Location getServerLocation() {
		return serverLocation;
	}

	public Location getClientLocation() {
		return clientLocation;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String _getStatus() {
		return status;
	}

	public String getStatus() {
		String current = status;
		status = "";
		return current;
	}

	public boolean itemCollected() {
		itemsCollected++;

		return itemsCollected == 13;
	}
	
}