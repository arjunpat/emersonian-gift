package items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Notification extends Item {

	private String text;
	private double created;
	private boolean isGood;
	private int length;
	private int borderLength = 1;

	private boolean done = false;

	private int x = 20;
	private int y = 720;

	public static int FINAL_Y = 720;
	public static int ANIMATE_WAIT_TIME = 5;

	public Notification(String text, boolean isGood) {
		this.text = text;
		this.created = System.currentTimeMillis();
		this.isGood = isGood;
		length = 1000;
	}

	public Notification(String text, boolean isGood, int length) {
		this.text = text;
		this.created = System.currentTimeMillis();
		this.length = length;
		this.isGood = isGood;
	}

	public boolean isOld() {
		return System.currentTimeMillis() > created + length;
	}

	public void draw(Graphics g) {
		/*if (isGood) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.RED);
		}
		g.drawRect(x - borderLength, y - borderLength, 400 + (2 * borderLength), 50 + (2 * borderLength));

		g.setColor(new Color(255, 255, 255, 220));
		g.fillRect(x, y, 400, 50);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Tahoma", Font.BOLD, 14));

		g.drawString(text, 40, y + 30);*/


		g.setColor(new Color(50, 50, 50, 240));
		g.fillRoundRect(x, y, 400, 50, 4, 4);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Tahoma", Font.PLAIN, 14));

		g.drawString(text, 40, y + 30);
	}

	public void moveUp() {
		y -= 3;
	}

	public void moveDown() {
		y += 3;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void addASecond() {
		created += 1000;
	}

	public void done() {
		done = true;
	}

	public boolean isDone() {
		return done;
	}
}