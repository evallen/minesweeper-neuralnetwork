package controller;

import java.awt.Color;
import java.io.File;

public class Parameters {
	// JFrame stuff
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 600;
	
	// Mine stuff
	public static final int NUM_MINES = 100;
	
	// BoardPanel stuff
	public static final int BORDER_PADDING = 20;
	public static final int BOARD_WIDTH = 1000;
	public static final int BOARD_HEIGHT = 600;
	
	public static final int PANEL_PREFERRED_WIDTH = BOARD_WIDTH + (2 * BORDER_PADDING);
	public static final int PANEL_PREFERRED_HEIGHT = BOARD_HEIGHT + (2 * BORDER_PADDING);
	
	// Genetic algorithm stuff
	//public final static int POPULATION_SIZE =70; // how many genomes are in the population
	//public final static int NUM_ELITISM = 8;
	//public final static int GENERATION_TIME = 1200; // length of generation in ticks
	//public static double MUTATION_RATE = 0.1;
	//public static double CROSSOVER_RATE = 0.7;
	//public final static double MUTATION_MULTIPLIER = .5; // how big the mutation can be - value of 1.0 symbolizes that weight can move max of 1.0 either direction
	//public final static int NUM_WEIGHTS_PER_GENOME = (11 * 15) + (16 * 2);
	//public final static double ROULETTE_MODIFIER = 2.0; // how much the top fitnesses are valued over lower ones.

	// MineSweeper stuff
	public final static int NUM_MINES_VISIBLE = 5; // how many mines the MineSweepers can see - MUST BE LESS THAN NUM_MINES
	public final static double MIN_SPEED = 6.0;

	// Neural network stuff
	public final static int NUM_HIDDEN_LAYERS = 2;
	public final static int NUM_INPUTS = 10;
	public final static int NUM_OUTPUTS = 2;
	public final static int[] NUM_NEURONS_PER_HIDDEN_LAYER = {15, 2};
	
	// Chart stuff
	public final static int CHARTPANEL_WIDTH = 700;
	public final static int CHARTPANEL_HEIGHT = 500;
	
	// Logging stuff
	public final static File FITTEST_GENOMES_LOG = new File("logs\\fittest_genomes.txt");
	
	// Mine stuff
	public static Object[][] MINE_SCORE_INFO = { // 
			{-20.0, 0.5, new Color(0x0000ff)},
			{4.0, 0.5, new Color(0xff0000)}
	};
}
