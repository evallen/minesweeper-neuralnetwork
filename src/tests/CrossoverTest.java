package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controller.Parameters;
import genalgorithm.GeneticAlgorithm;
import genalgorithm.Genome;

public class CrossoverTest {

	Genome gA;
	Genome gB;
	
	@Before
	public void setUp() throws Exception {
		Parameters.MUTATION_RATE = 1.0;
		Parameters.CROSSOVER_RATE = 1.0;
		
		List<Double> weights1 = new ArrayList<Double>();
		weights1.add(1.1);
		weights1.add(1.2);
		weights1.add(1.3);
		weights1.add(1.4);
		weights1.add(1.5);
		
		List<Double> weights2 = new ArrayList<Double>();
		weights2.add(2.1);
		weights2.add(2.2);
		weights2.add(2.3);
		weights2.add(2.4);
		weights2.add(2.5);
		
		gA = new Genome (weights1);
		gB = new Genome (weights2);
		
		System.out.println(gA.weights);
		System.out.println(gB.weights);
		System.out.println();
	}

	@Test
	public void testCrossover() {
		Genome[] children = GeneticAlgorithm.crossover(gA, gB);
		System.out.println("Crossover: ");
		System.out.println(children[0].weights);
		System.out.println(children[1].weights);
		System.out.println();
	}

	@Test
	public void testMutate() {
		System.out.println("Mutate: ");
		GeneticAlgorithm.mutate(gA);
		GeneticAlgorithm.mutate(gB);
		System.out.println(gA.weights);
		System.out.println(gB.weights + "\n\n===========\n");
	}

}
