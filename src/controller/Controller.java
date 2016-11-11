package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import chart.ChartFrame;
import genalgorithm.GeneticAlgorithm;

public class Controller {
	
	public static File XMLFile = new File("controller\\params.xml");
	static GeneticAlgorithm ga = new GeneticAlgorithm(XMLFile);
		
	public static MineSweeper[] sweepers = new MineSweeper[ga.populationSize];
	public static Mine[] mines = new Mine[Parameters.NUM_MINES];
	
	private static double lastUpdateTime = 0; // in milliseconds
	private static double deltaTime = 0; // in seconds
	
	static double ticksPerSecond = 60;
	static double msBetweenTicks = (1 / ticksPerSecond) * 1000;
	static double tickMultiplier = 1.0;
	
	static BoardPanel panel;
	
	static int totalTicks = 0;
	static int epochTicks = 0;
	
	public static ChartFrame cFrame;
	
	public static void main (String[] args) {
		
		cFrame = new ChartFrame("Chart");
		cFrame.pack();
		cFrame.setVisible(true);
		
		JFrame frame = new JFrame("Mine Sweeper Neural Network");
		panel = new BoardPanel();
		
		panel.addMouseListener(panel);
		panel.getInputMap().put(KeyStroke.getKeyStroke('j'), "slow");
		panel.getInputMap().put(KeyStroke.getKeyStroke('k'), "fast");
		panel.getInputMap().put(KeyStroke.getKeyStroke('n'), "smallSlow");
		panel.getInputMap().put(KeyStroke.getKeyStroke('m'), "smallFast");
		panel.getInputMap().put(KeyStroke.getKeyStroke('f'), "paintToggle");
		panel.getActionMap().put("slow", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				tickMultiplier -= 20;
				tickMultiplier = Math.max(1.0, tickMultiplier);
			}
			
		});
		panel.getActionMap().put("fast", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				tickMultiplier += 20;
				
			}
			
		});
		panel.getActionMap().put("smallSlow", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				tickMultiplier -= 0.1;
				tickMultiplier = Math.max(tickMultiplier,  0.1);
			}
			
		});
		panel.getActionMap().put("smallFast", new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				tickMultiplier += 0.1;
			}
		});
		panel.getActionMap().put("paintToggle", new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (panel.painting)
					panel.painting = false;
				else
					panel.painting = true;
			}
		});
		BorderLayout layout = new BorderLayout();
		
		frame.getContentPane().setLayout(layout);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(WIDTH, HEIGHT);
		
		frame.setFocusable(true);
		
		frame.setVisible(true);
		
		panel.requestFocus();
		
		ga.start();
		
	}
	
	public static void startLoop () {
		epochTicks = 0;
		Random rand = new Random();		
		clearAndCreateMines(rand);
		runGameLoop();
		
		for (MineSweeper sweeper : sweepers) {
			sweeper.epochEnd();
		}
	}
	
	public static void runGameLoop() {
		lastUpdateTime = System.currentTimeMillis();
		while (true) {
			deltaTime = System.currentTimeMillis() - lastUpdateTime;
			
			if (deltaTime < msBetweenTicks / tickMultiplier)
				continue;
			
			lastUpdateTime = System.currentTimeMillis();
			tick();
			totalTicks++;
			epochTicks++;
			
			if (epochTicks >= ga.generationTime) {
				return;
			}
		}
	}
	
	public static void tick() {
		for (MineSweeper sweeper: sweepers) {
			sweeper.doTurn();
		}
		checkMineCollisions();
		if (panel.painting) {
			panel.repaint();
		}
	}
	
	public static double getDeltaTimeSec() {
		return (deltaTime * tickMultiplier) / 1000;
	}
	
	public static double getDeltaTimeMs() {
		return deltaTime * tickMultiplier;
	}
	
	/**
	 * Checks to see if any mine sweepers have hit a mine - if they have, increment the minesweeper's
	 * mine counter, delete the mine, and put a new one somewhere. 
	 */
	public static void checkMineCollisions() {
		for (int i = 0; i < mines.length; i++) {
			MineSweeper closestSweeper = null;
			double closestDistance = Double.POSITIVE_INFINITY;
			for (int j = 0; j < sweepers.length; j++) {
				double distance = getDistance(sweepers[j].x, sweepers[j].y, mines[i].x, mines[i].y);
				if (distance <= MineSweeper.collectDistance && distance < closestDistance) {
					closestDistance = distance;
					closestSweeper = sweepers[j];
				}
			}
			if (closestSweeper != null) {
				closestSweeper.collectedMine(mines[i]);
				Random rand = new Random();
				int x = rand.nextInt(Parameters.BOARD_WIDTH) + Parameters.BORDER_PADDING;
				int y = rand.nextInt(Parameters.BOARD_HEIGHT) + Parameters.BORDER_PADDING;
				try {
					mines[i] = new Mine(x, y);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public static void clearAndCreateMines(Random rand) {
		for (int i = 0; i < mines.length; i++) {
			int x = rand.nextInt(Parameters.BOARD_WIDTH) + Parameters.BORDER_PADDING;
			int y = rand.nextInt(Parameters.BOARD_HEIGHT) + Parameters.BORDER_PADDING;
			try {
				mines[i] = new Mine(x, y);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
