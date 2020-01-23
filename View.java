import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public abstract class View extends JPanel {

	protected Color green = new Color(79, 255, 146);
	protected Color white = new Color(255, 255, 255);
	protected Color purple = new Color(192, 179, 224);
	protected Color lblue = new Color(176, 224, 230);
	protected Color blue = new Color(0, 0, 255);
	protected Color red = new Color(255, 0, 0);
	protected Color black = new Color(0, 0, 0);
	protected Color yellow = new Color(244, 236, 8);
	protected Color dblue = new Color(30, 144, 255);
	protected Color red1 = new Color(242, 106, 117);
	protected Color yellow1 = new Color(249, 228, 89);

	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 800;
	
	public View() {
		this.setLayout(null);
		this.setFocusable(true);
	}

	public Dimension getPreferredSize() {
		return new Dimension(this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
	}

	public final void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(white);
		g.fillRect(0, 0, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
		this.draw(g);
	}

	protected void drawTitle(Graphics g, Color c, String text, int x, int y) {
		g.setColor(black);
		g.setFont(new Font("Tahoma", Font.PLAIN, 24));
		g.drawString(text, x, y);
		g.setColor(c);
		g.fillRect(x, y + 5, (int)(text.length() * 11.2), 3);
	}

	protected void drawBigTitle(Graphics g, Color c, String text, int x, int y) {
		g.setColor(black);
		g.setFont(new Font("Tahoma", Font.PLAIN, 36));
		g.drawString(text, x, y);
		g.setColor(c);
		g.fillRect(x, y + 5, text.length() * 18, 4);
	}

	public abstract void draw(Graphics g);
}
