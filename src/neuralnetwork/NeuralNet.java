package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuralNet
{

    public List<NeuronLayer> layers = new ArrayList<>();
    private int numInputs;
    private int numLayers;
    private List<Integer> numNeuronsPerLayer;
    private List<Neuron.ActivationType> activationTypes;

    /**
     * Constructor for the main neural net.
     *
     * @param numInputs          The number of inputs to the net.
     * @param numLayers          The number of layers in the net. This includes the output layer but does not include the input layer.
     * @param numNeuronsPerLayer The number of neurons per each layer.
     * @param weights            The weights for the neural net.
     */
    public NeuralNet(int numInputs, int numLayers, List<Integer> numNeuronsPerLayer,
                     List<Neuron.ActivationType> activationTypes, List<Double> weights) throws IllegalParameterException
    {
        assert(activationTypes.size() == numLayers + 2); // Otherwise something is wrong.
        this.numInputs = numInputs;
        this.numLayers = numLayers;
        this.numNeuronsPerLayer = numNeuronsPerLayer;
        this.activationTypes = activationTypes;
        validateActivationTypes();

        makeNet(weights);
    }

    private void makeNet(List<Double> weights)
    {
        int start = 0;
        int end = numNeuronsPerLayer.get(0) * (numInputs + 1);
        layers.add(new NeuronLayer(weights.subList(start, end), numNeuronsPerLayer.get(0), numInputs + 1,
                activationTypes.get(0)));
        for (int i = 1; i < numLayers; i++)
        {
            start = end;
            end += numNeuronsPerLayer.get(i) * (numNeuronsPerLayer.get(i - 1) + 1); // Add one for bias
            layers.add(new NeuronLayer(weights.subList(start, end), numNeuronsPerLayer.get(i),
                    numNeuronsPerLayer.get(i - 1) + 1,
                    activationTypes.get(i)));
        }
    }

    public List<Double> run(List<Double> inputs)
    {
        if (inputs.size() != numInputs)
            throw new IllegalArgumentException("Input list must match numInputs. (inputs.size() -> " + inputs.size() + " and numInputs -> " + numInputs);

        for (int i = 0; i < numLayers; i++)
        {
            inputs = layers.get(i).run(inputs);
        }

        return inputs;
    }

    private void validateActivationTypes() throws IllegalParameterException
    {
        for (int i = 0; i < activationTypes.size(); i++)
        {
            if (activationTypes.get(i) == Neuron.ActivationType.ACT_UNDEF)
                throw new IllegalParameterException("Activation type for layer #" + i + " (zero-indexed) is invalid!");
        }
    }

}
