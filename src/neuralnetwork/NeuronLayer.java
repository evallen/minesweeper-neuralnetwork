package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuronLayer {
	
	int numNeurons;
	int numInputsPerNeuron;
	public List<Neuron> neurons = new ArrayList<Neuron>();
	
	public NeuronLayer(int numNeurons, int numInputsPerNeuron) {
		this.numNeurons = numNeurons;
		this.numInputsPerNeuron = numInputsPerNeuron;
		for (int i = 0; i < numNeurons; i++) {
			neurons.add(new Neuron(numInputsPerNeuron));
		}
	}
	
	public NeuronLayer(List<Double> weights, int numNeurons, int numInputsPerNeuron) {
		this.numInputsPerNeuron = numInputsPerNeuron;
		this.numNeurons = numNeurons;
		for (int i = 0; i < numNeurons; i++) {
			neurons.add(new Neuron(weights.subList(numInputsPerNeuron * i, numInputsPerNeuron * (i + 1))));
		}
	}
	
	public List<Double> run(List<Double> inputs) {
		if (inputs.size() + 1 != numInputsPerNeuron) 
			throw new IllegalArgumentException("Input list must match numInputs. (inputs.size() + 1 -> " + (inputs.size() + 1) + " and numInputsPerNeuron -> " + numInputsPerNeuron);
		
		List<Double> outputs = new ArrayList<Double>();
		for (int i = 0; i < numNeurons; i++) {
			outputs.add(neurons.get(i).run(inputs));
		}
		
		return outputs;
	}

}
