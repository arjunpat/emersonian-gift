package items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Player extends Item {

	private BufferedImage picture;
	private int moveConstant = 100;
	private int itemsCollected;

	public Player(int x, int y, String filename) {
		super.x = x;
		super.y = y;

		try {
			picture = ImageIO.read(getClass().getResource(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {

		g.drawImage(picture, x, y, null);
	}

	public void moveUp() {
		if (y + moveConstant <= 800)
			y += moveConstant;
	}

	public void moveDown() {
		if (y - moveConstant >= 0)
			y -= moveConstant;
	}

	public void moveLeft() {
		if (x - moveConstant >= 0)
			x -= moveConstant;
	}

	public void moveRight() {
		if (x + moveConstant <= 800)
			x += moveConstant;
	}

	public void moveTo(int x, int y) {
		super.x = x;
		super.y = y;
	}

	public void itemCollected() {
		itemsCollected++;
	}

	public int getItemsCollected() {
		return itemsCollected;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public int getX() { return x; }

	public int getY() { return y; }

}