package tests;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import neuralnetwork.NeuralNet;

public class NeuralNetTest {

	List<Double> weights = new ArrayList<Double>();
	
	@Before
	public void setUp() throws Exception {
		weights.add(0.0);
		weights.add(-.3);
		weights.add(.7);
		
		weights.add(0.0);
		weights.add(3.3);
		weights.add(-2.4);
		
		weights.add(0.0);
		weights.add(.4);
		weights.add(-.7);
		
		weights.add(1.0);
		weights.add(1.3);
		weights.add(-1.9);
	}

	@Test
	public void testNeuralNetIntIntIntIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testNeuralNetIntIntIntIntArrayListOfDouble() {
		int[] neuronsPerHiddenLayer = {2,2};
		NeuralNet net = new NeuralNet(2, 2, 2, neuronsPerHiddenLayer, weights);
		for (int i = 0; i < net.layers.size(); i++) {
			System.out.println("Layer " + i);
			for (int j = 0; j < net.layers.get(i).neurons.size(); j++) {
				System.out.println("\tNeuron " + j); 
				for (int k = 0; k < net.layers.get(i).neurons.get(j).weights.size(); k++) {
					System.out.println("\t\t" + net.layers.get(i).neurons.get(j).weights.get(k));
				}
			}
		}
		
		List<Double> inputs = new ArrayList<Double>();
		inputs.add(3.2);
		inputs.add(2.4);
		System.out.println(net.run(inputs));
	}

}
