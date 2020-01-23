import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ResultScreen extends View {

	private boolean won;
	private String why;
	
	public ResultScreen(boolean won, String why) {
		this.won = won;
		this.why = why;
	}

	public void draw(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(new Font("Tahoma", Font.BOLD, 58));
		
		if (won) {
			g.drawString("You Win!", 20, 100);
		} else {
			g.drawString("You Lose!", 20, 100);
		}

		g.setFont(new Font("Tahoma", Font.PLAIN, 22));
		g.drawString(why, 20, 160);

	}
}