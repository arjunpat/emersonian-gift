package game;

import java.io.Serializable;

public class Location implements Serializable {

	private int x, y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object l) {
		return ((Location)l).hashCode() == hashCode();
	}

}