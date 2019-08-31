package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuronLayer
{

    private int numNeurons;
    private int numInputsPerNeuron;
    public List<Neuron> neurons = new ArrayList<>();

    NeuronLayer(List<Double> weights, int numNeurons, int numInputsPerNeuron, Neuron.ActivationType activationType)
    {
        this.numInputsPerNeuron = numInputsPerNeuron;
        this.numNeurons = numNeurons;
        for (int i = 0; i < numNeurons; i++)
        {
            neurons.add(new Neuron(weights.subList(numInputsPerNeuron * i, numInputsPerNeuron * (i + 1)),
                    activationType));
        }
    }

    List<Double> run(List<Double> inputs)
    {
        if (inputs.size() + 1 != numInputsPerNeuron)
            throw new IllegalArgumentException("Input list must match numInputs. (inputs.size() + 1 -> " + (inputs.size() + 1) + " and numInputsPerNeuron -> " + numInputsPerNeuron);

        List<Double> outputs = new ArrayList<>();
        for (int i = 0; i < numNeurons; i++)
        {
            outputs.add(neurons.get(i).run(inputs));
        }

        return outputs;
    }

}
