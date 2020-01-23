package items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public abstract class Item {
	protected int x, y, height, width;

	public Item() {

	}

	public abstract void draw(Graphics g);

	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
