package genalgorithm;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import controller.Controller;
import controller.MineSweeper;
import controller.Parameters;
import neuralnetwork.NeuralNet;

public class GeneticAlgorithm {
	
	public Genome fittestGenome = new Genome(null);
	
	public int generations = 0;
	
	public double totalFitness;
	public List<Double> bestFitnesses = new ArrayList<Double>();
	public List<Double> worstFitnesses = new ArrayList<Double>();
	public List<Double> avgFitnesses = new ArrayList<Double>();
	
	List<Genome> genomes = new ArrayList<Genome>();
	
	public GeneticAlgorithm () {
		// default ctor
	}
	
	public void start() {
		generateRandomGenomes();
		while (true) {
			generations++;
			runEpoch();
		}
	}
	
	private void generateRandomGenomes() {
		Random rand = new Random();
		
		for (int i = 0; i < Parameters.POPULATION_SIZE; i++) {
			List<Double> randomWeights = new ArrayList<Double>();
			for (int j = 0; j < Parameters.NUM_WEIGHTS_PER_GENOME; j++) {
				randomWeights.add((rand.nextDouble() * 2) - 1);
			}
			genomes.add(new Genome(randomWeights));
		}
	}
	
	public static Genome[] crossover(Genome mom, Genome dad) {
		Genome[] children = new Genome[2];
		Random rand = new Random();
		
		if (rand.nextDouble() > Parameters.CROSSOVER_RATE || mom.weights == dad.weights) {
			children[0] = mom;
			children[1] = dad;
			return children;
		}
		
		int switchIndex = rand.nextInt(mom.weights.size());
		
		List<Double> newWeights0 = new ArrayList<Double>();
		newWeights0.addAll(mom.weights.subList(0, switchIndex));
		newWeights0.addAll(dad.weights.subList(switchIndex, dad.weights.size()));
		
		List<Double> newWeights1 = new ArrayList<Double>();
		newWeights1.addAll(dad.weights.subList(0, switchIndex));
		newWeights1.addAll(mom.weights.subList(switchIndex, mom.weights.size()));
		
		children[0] = new Genome(newWeights0);
		children[1] = new Genome(newWeights1);
		
		return children;
	}
	
	public static void mutate(Genome genome) {
		Random rand = new Random();
		
		List<Double> newWeights = new ArrayList<Double>();
		for (Double weight : genome.weights) {
			if (rand.nextDouble() < Parameters.MUTATION_RATE) {
				newWeights.add(weight + (rand.nextDouble() * 2 * Parameters.MUTATION_MULTIPLIER) - Parameters.MUTATION_MULTIPLIER); // changes by random value between -1.0 and 1.0
				
			} else {
				newWeights.add(weight);
			}
		}
		
		genome.weights = newWeights;
	}
	
	private void runEpoch() {
		// create MineSweepers
		createMineSweepers();
		
		// evaluate fitness of population
		Controller.startLoop();
		setGenomeFitnesses();
		
		// sort by fitness
		sortGenomes();
		/*for (int i = 0; i < genomes.size(); i++) {
			for (Double dbl : genomes.get(i).weights) {
				System.out.print(dbl + " ");
			}
			System.out.println();
		}*/
		
		// find best fitness, worst fitness, and average fitness
		setBestWorstAvgStats();
		
		// elitism - we keep a few of the best genomes
		List<Genome> newPop = new ArrayList<Genome>();
		for (int i = 0; i < Parameters.NUM_ELITISM; i++) {
			newPop.add(genomes.get(i));
		}
		
		// crossover
		while (newPop.size() < Parameters.POPULATION_SIZE) {
			Genome[] newGenomes = crossover(genomes.get(selectGenomeRoulette(genomes)), genomes.get(selectGenomeRoulette(genomes)));
			
			//mutate
			GeneticAlgorithm.mutate(newGenomes[0]);
			GeneticAlgorithm.mutate(newGenomes[1]);
			
			newPop.add(newGenomes[0]);
			newPop.add(newGenomes[1]);
		}
		
		// take out extra genome if one was added during crossover (since each crossover gives two but the population size or elitism size may have been odd)
		if (newPop.size() == Parameters.POPULATION_SIZE) {
			//newPop.remove(newPop.size() - 1);
		}
		
		genomes = newPop;
	}
	
	private int selectGenomeRoulette(List<Genome> gList) {
		// genomes list must be sorted by this point!
		double[] cumulativeWheel = new double[gList.size()];
		double worstFitness = genomes.get(genomes.size() - 1).fitness; // Used for when we have negative fitness values - we just recenter all of them so the worst is at 0 so we can use this method
		if (worstFitness > 0) {
			worstFitness = 0; // if there is no negative fitness values, just make this 0 and don't change anything.
		}
		cumulativeWheel[0] = genomes.get(0).fitness - worstFitness;
		for (int i = 1; i < cumulativeWheel.length; i++) {
			cumulativeWheel[i] = cumulativeWheel[i-1] + Math.pow(genomes.get(i).fitness - worstFitness, Parameters.ROULETTE_MODIFIER);
		}
		
		Random rng = new Random();
		double randomNum = rng.nextDouble() * cumulativeWheel[genomes.size() - 1];
		
		int index = Arrays.binarySearch(cumulativeWheel, randomNum);
		
		if (index < 0) {
			index = Math.abs(index + 1);
		}
		
		return index;
	}
	
	private void sortGenomes() {
		Comparator<Genome> comp = (Genome a, Genome b) -> {
			return b.compareTo(a);
		};
		genomes.sort(comp);
	}
	
	private void createMineSweepers () {
		
		Random rand = new Random();
				
		for (int i = 0; i < Parameters.POPULATION_SIZE; i++) {
			int x = rand.nextInt(Parameters.BOARD_WIDTH) + Parameters.BORDER_PADDING;
			int y = rand.nextInt(Parameters.BOARD_HEIGHT) + Parameters.BORDER_PADDING;
			int heading = rand.nextInt(360); 
			NeuralNet net = new NeuralNet(Parameters.NUM_INPUTS, Parameters.NUM_OUTPUTS, 
					Parameters.NUM_HIDDEN_LAYERS, Parameters.NUM_NEURONS_PER_HIDDEN_LAYER, genomes.get(i).weights);
			try {
				Controller.sweepers[i] = new MineSweeper(x, y, heading, net);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setGenomeFitnesses() {
		for (int i = 0; i < Parameters.POPULATION_SIZE; i++) {
			genomes.get(i).fitness = Controller.sweepers[i].score;
		}
	}
	
	private void setBestWorstAvgStats() {
		Controller.cFrame.dataset.addValue((Number) genomes.get(0).fitness, "Best Fitness", generations);
		double avgFitness = 0.0;
		for (int i = 0; i < genomes.size(); i++) {
			avgFitness += genomes.get(i).fitness;
		}
		avgFitness /= genomes.size();		
		Controller.cFrame.dataset.addValue((Number) avgFitness, "Average Fitness", generations);
		Controller.cFrame.dataset.addValue((Number) genomes.get(genomes.size() - 1).fitness, "Worst Fitness", generations);
		
		if (genomes.get(0).fitness > fittestGenome.fitness && generations % 10 == 0) {
			List<String> lines = new ArrayList<String>();
			lines.add("" + generations + " " +  genomes.get(0).weights);
			try {
				if (!Parameters.FITTEST_GENOMES_LOG.exists()) {
					Parameters.FITTEST_GENOMES_LOG.getParentFile().mkdirs();
					Parameters.FITTEST_GENOMES_LOG.createNewFile();	
				}
				Files.write(Parameters.FITTEST_GENOMES_LOG.toPath(), lines, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

}
