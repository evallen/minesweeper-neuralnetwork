package controller;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6388172060034471018L;
	
	public boolean painting = true;

	public BoardPanel () {
		super();
		System.out.println("Board dimensions are: (" + Parameters.PANEL_PREFERRED_WIDTH + ", " + Parameters.PANEL_PREFERRED_HEIGHT + ")");
		this.setPreferredSize(new Dimension (Parameters.PANEL_PREFERRED_WIDTH, Parameters.PANEL_PREFERRED_HEIGHT));
		this.setFocusable(true);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension (Parameters.PANEL_PREFERRED_WIDTH, Parameters.PANEL_PREFERRED_HEIGHT);
	}
	
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		//g.drawString("Test.", 10, 10 + g.getFontMetrics().getAscent());
		
		// Draw border
		g.drawRect(Parameters.BORDER_PADDING, Parameters.BORDER_PADDING, Parameters.BOARD_WIDTH, Parameters.BOARD_HEIGHT);
		
		if (painting) {
			for (Mine mine : Controller.mines) {
				mine.paintMine(g);
			}
			
			for (MineSweeper sweeper : Controller.sweepers) {
				sweeper.paintMineSweeper(g);
			}
		}
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Generation: " + Controller.ga.generations, Parameters.BORDER_PADDING + 10, Parameters.BORDER_PADDING + 10 + g2d.getFontMetrics().getAscent());
		g2d.drawString("Tick Multiplier: " + Controller.tickMultiplier + "x", Parameters.BORDER_PADDING + 10, Parameters.BORDER_PADDING + 10 + g2d.getFontMetrics().getAscent() + 2 + g2d.getFontMetrics().getAscent());
		g2d.dispose();
	}
	
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Click @ (" + e.getX() + ", " + e.getY() + ")");
		this.requestFocus();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Key released!");
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("Key released!");
		if (e.getKeyCode() == KeyEvent.VK_J) {
			//Controller.rate++;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_K) {
			//Controller.rate--;
		}
		
		//Controller.rate = Math.max(1.0, Controller.rate);
	}
	
	
}
