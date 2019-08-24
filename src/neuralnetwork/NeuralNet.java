package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuralNet
{

    public List<NeuronLayer> layers = new ArrayList<NeuronLayer>();
    private int numInputs;
    private int numOutputs;
    private int numHiddenLayers;
    private List<Integer> numNeuronsPerHiddenLayer;

    /**
     * Constructor for the main neural net.
     *
     * @param numInputs                The number of inputs to the net.
     * @param numOutputs               The number of outputs the net will produce.
     * @param numHiddenLayers          The number of hidden layers in the net. This includes the output layer but does not include the input layer.
     * @param numNeuronsPerHiddenLayer The number of neurons per each hidden layer.
     */
    public NeuralNet(int numInputs, int numOutputs, int numHiddenLayers, List<Integer> numNeuronsPerHiddenLayer)
    {
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        this.numHiddenLayers = numHiddenLayers;
        this.numNeuronsPerHiddenLayer = numNeuronsPerHiddenLayer;

        makeNet();
    }

    /**
     * Constructor for the main neural net.
     *
     * @param numInputs                The number of inputs to the net.
     * @param numOutputs               The number of outputs the net will produce.
     * @param numHiddenLayers          The number of hidden layers in the net. This includes the output layer but does not include the input layer.
     * @param numNeuronsPerHiddenLayer The number of neurons per each hidden layer.
     */
    public NeuralNet(int numInputs, int numOutputs, int numHiddenLayers, List<Integer> numNeuronsPerHiddenLayer, List<Double> weights)
    {
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        this.numHiddenLayers = numHiddenLayers;
        this.numNeuronsPerHiddenLayer = numNeuronsPerHiddenLayer;

        makeNet(weights);
    }

    public void makeNet()
    { // TODO Add logic for dedicated output layer (instead of using a final "hidden" one)
        layers.add(new NeuronLayer(numNeuronsPerHiddenLayer.get(0), numInputs));
        for (int i = 1; i < numHiddenLayers; i++) {
            layers.add(new NeuronLayer(numNeuronsPerHiddenLayer.get(i), numNeuronsPerHiddenLayer.get(i - 1)));
        }
    }

    public void makeNet(List<Double> weights)
    {
        int start = 0;
        int end = numNeuronsPerHiddenLayer.get(0) * (numInputs + 1);
        layers.add(new NeuronLayer(weights.subList(start, end), numNeuronsPerHiddenLayer.get(0), numInputs + 1));
        for (int i = 1; i < numHiddenLayers; i++) {
            start = end;
            end += numNeuronsPerHiddenLayer.get(i) * (numNeuronsPerHiddenLayer.get(i - 1) + 1);
            layers.add(new NeuronLayer(weights.subList(start, end), numNeuronsPerHiddenLayer.get(i), numNeuronsPerHiddenLayer.get(i - 1) + 1));
        }
    }

    public List<Double> run(List<Double> inputs)
    {
        if (inputs.size() != numInputs)
            throw new IllegalArgumentException("Input list must match numInputs. (inputs.size() -> " + inputs.size() + " and numInputs -> " + numInputs);

        for (int i = 0; i < numHiddenLayers; i++) {
            inputs = layers.get(i).run(inputs);
        }

        return inputs;
    }

    public int getNumInputs()
    {
        return numInputs;
    }

    public int getNumOutputs()
    {
        return numOutputs;
    }

}
